package com.listaai.list.adapter.inbound.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.listaai.list.adapter.outbound.persistence.repository.ShoppingListJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ShoppingListIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShoppingListJpaRepository shoppingListJpaRepository;

    @Test
    void shouldCreateAndFetchShoppingListUsingRealHttpFlow() throws Exception {
        JsonNode createdList = postForJson("""
                {
                  "name": "Churrasco",
                  "items": [
                    {
                      "name": "Carvao",
                      "quantity": 2,
                      "unit": "UN"
                    }
                  ],
                  "participants": [
                    {
                      "name": "Eduardo",
                      "phoneNumber": "11999999999"
                    }
                  ]
                }
                """, "/lists");

        Long listId = createdList.get("id").asLong();

        var persistedList = shoppingListJpaRepository.findById(listId);

        assertThat(persistedList).isPresent();
        assertThat(persistedList.get().getName()).isEqualTo("Churrasco");
        assertThat(persistedList.get().getItems()).hasSize(1);
        assertThat(persistedList.get().getParticipants()).hasSize(1);
        assertThat(persistedList.get().getItems().iterator().next().getName()).isEqualTo("Carvao");
        assertThat(persistedList.get().getParticipants().iterator().next().getPhoneNumber()).isEqualTo("11999999999");

        mockMvc.perform(get("/lists/{id}", listId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(listId))
                .andExpect(jsonPath("$.name").value("Churrasco"))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].name").value("Carvao"))
                .andExpect(jsonPath("$.participants.length()").value(1))
                .andExpect(jsonPath("$.participants[0].name").value("Eduardo"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingInvalidShoppingList() throws Exception {
        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "items": [],
                                  "participants": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("name: Shopping list name must not be blank"))
                .andExpect(jsonPath("$.path").value("/lists"));
    }

    @Test
    void shouldReturnNotFoundWhenFetchingMissingShoppingList() throws Exception {
        mockMvc.perform(get("/lists/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Shopping list not found"))
                .andExpect(jsonPath("$.path").value("/lists/999999"));
    }

    @Test
    void shouldUpdateAndDeleteShoppingListUsingRealHttpFlow() throws Exception {
        JsonNode createdList = createList("Feira", "Tomate", "Ana");
        Long listId = createdList.get("id").asLong();

        mockMvc.perform(patch("/lists/{id}", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Feira de domingo"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(listId))
                .andExpect(jsonPath("$.name").value("Feira de domingo"));

        assertThat(shoppingListJpaRepository.findById(listId))
                .isPresent()
                .get()
                .extracting(list -> list.getName())
                .isEqualTo("Feira de domingo");

        mockMvc.perform(delete("/lists/{id}", listId))
                .andExpect(status().isOk());

        assertThat(shoppingListJpaRepository.findById(listId)).isEmpty();

        mockMvc.perform(get("/lists/{id}", listId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Shopping list not found"));
    }

    @Test
    void shouldManageItemsUsingRealHttpFlow() throws Exception {
        JsonNode createdList = postForJson("""
                {
                  "name": "Mercado",
                  "items": [],
                  "participants": [
                    {
                      "name": "Eduardo",
                      "phoneNumber": "11999999999"
                    }
                  ]
                }
                """, "/lists");

        Long listId = createdList.get("id").asLong();

        JsonNode listAfterAdd = postForJson("""
                {
                  "name": "Cafe",
                  "quantity": 1,
                  "unit": "PACK"
                }
                """, "/lists/" + listId + "/items");

        JsonNode addedItem = findItemByName(listAfterAdd, "Cafe");
        Long itemId = addedItem.get("id").asLong();

        assertPersistedItem(listId, "Cafe", 1, "PACK", false);

        JsonNode listAfterUpdate = patchForJson("""
                {
                  "name": "Cafe especial",
                  "quantity": 2,
                  "unit": "BOX"
                }
                """, "/lists/" + listId + "/items/" + itemId);

        JsonNode updatedItem = findItemByName(listAfterUpdate, "Cafe especial");
        assertThat(updatedItem.get("id").asLong()).isEqualTo(itemId);
        assertThat(updatedItem.get("quantity").asInt()).isEqualTo(2);
        assertThat(updatedItem.get("unit").asText()).isEqualTo("BOX");
        assertPersistedItem(listId, "Cafe especial", 2, "BOX", false);

        JsonNode listAfterPurchase = patchForJson("""
                {
                  "purchased": true
                }
                """, "/lists/" + listId + "/items/" + itemId + "/purchase");

        JsonNode purchasedItem = findItemByName(listAfterPurchase, "Cafe especial");
        assertThat(purchasedItem.get("purchased").asBoolean()).isTrue();
        assertPersistedItem(listId, "Cafe especial", 2, "BOX", true);

        JsonNode listAfterDelete = deleteForJson("/lists/" + listId + "/items/" + itemId);
        assertThat(listAfterDelete.get("items")).isEmpty();
        assertThat(shoppingListJpaRepository.findById(listId)).isPresent();
        assertThat(shoppingListJpaRepository.findById(listId).orElseThrow().getItems()).isEmpty();
    }

    @Test
    void shouldManageParticipantsUsingRealHttpFlow() throws Exception {
        JsonNode createdList = postForJson("""
                {
                  "name": "Viagem",
                  "items": [
                    {
                      "name": "Mala",
                      "quantity": 1,
                      "unit": "UN"
                    }
                  ],
                  "participants": []
                }
                """, "/lists");

        Long listId = createdList.get("id").asLong();

        JsonNode listAfterAdd = postForJson("""
                {
                  "name": "Maria",
                  "phoneNumber": "11988887777"
                }
                """, "/lists/" + listId + "/participants");

        JsonNode addedParticipant = findParticipantByName(listAfterAdd, "Maria");
        Long participantId = addedParticipant.get("id").asLong();

        assertPersistedParticipant(listId, "Maria", "11988887777");

        JsonNode listAfterUpdate = patchForJson("""
                {
                  "name": "Maria Silva",
                  "phoneNumber": "11977776666"
                }
                """, "/lists/" + listId + "/participants/" + participantId);

        JsonNode updatedParticipant = findParticipantByName(listAfterUpdate, "Maria Silva");
        assertThat(updatedParticipant.get("id").asLong()).isEqualTo(participantId);
        assertThat(updatedParticipant.get("phoneNumber").asText()).isEqualTo("11977776666");
        assertPersistedParticipant(listId, "Maria Silva", "11977776666");

        JsonNode listAfterDelete = deleteForJson("/lists/" + listId + "/participants/" + participantId);
        assertThat(listAfterDelete.get("participants")).isEmpty();
        assertThat(shoppingListJpaRepository.findById(listId)).isPresent();
        assertThat(shoppingListJpaRepository.findById(listId).orElseThrow().getParticipants()).isEmpty();
    }

    private JsonNode createList(String listName, String itemName, String participantName) throws Exception {
        return postForJson("""
                {
                  "name": "%s",
                  "items": [
                    {
                      "name": "%s",
                      "quantity": 1,
                      "unit": "UN"
                    }
                  ],
                  "participants": [
                    {
                      "name": "%s",
                      "phoneNumber": "11999999999"
                    }
                  ]
                }
                """.formatted(listName, itemName, participantName), "/lists");
    }

    private JsonNode postForJson(String body, String path) throws Exception {
        MvcResult result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private JsonNode patchForJson(String body, String path) throws Exception {
        MvcResult result = mockMvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private JsonNode deleteForJson(String path) throws Exception {
        MvcResult result = mockMvc.perform(delete(path))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private JsonNode findItemByName(JsonNode listResponse, String itemName) {
        return streamArray(listResponse.get("items"))
                .filter(item -> itemName.equals(item.get("name").asText()))
                .findFirst()
                .orElseThrow();
    }

    private JsonNode findParticipantByName(JsonNode listResponse, String participantName) {
        return streamArray(listResponse.get("participants"))
                .filter(participant -> participantName.equals(participant.get("name").asText()))
                .findFirst()
                .orElseThrow();
    }

    private java.util.stream.Stream<JsonNode> streamArray(JsonNode arrayNode) {
        return java.util.stream.StreamSupport.stream(arrayNode.spliterator(), false);
    }

    private void assertPersistedItem(Long listId, String itemName, int quantity, String unit, boolean purchased) {
        var persistedList = shoppingListJpaRepository.findById(listId).orElseThrow();
        var item = persistedList.getItems().stream()
                .filter(savedItem -> itemName.equals(savedItem.getName()))
                .findFirst()
                .orElseThrow();

        assertThat(item.getQuantity()).isEqualTo(quantity);
        assertThat(item.getUnit().name()).isEqualTo(unit);
        assertThat(item.isPurchased()).isEqualTo(purchased);
    }

    private void assertPersistedParticipant(Long listId, String participantName, String phoneNumber) {
        var persistedList = shoppingListJpaRepository.findById(listId).orElseThrow();
        var participant = persistedList.getParticipants().stream()
                .filter(savedParticipant -> participantName.equals(savedParticipant.getName()))
                .findFirst()
                .orElseThrow();

        assertThat(participant.getPhoneNumber()).isEqualTo(phoneNumber);
    }
}
