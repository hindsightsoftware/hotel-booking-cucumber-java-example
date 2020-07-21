package com.hindsightsoftware.hotelbooking;

import com.hindsightsoftware.hotelbooking.utils.AuthorizationToken;
import io.cucumber.java.en.Given;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.List;

public class StepDefinitions {
    private static final String BASE_URL = "http://localhost:8080";
    private JSONObject requestBody;
    private HttpResponse response;
    private long lastBookingId;

    @Given("a user wants to make a booking with the following details")
    public void a_user_wants_to_make_a_booking_with_the_following_details(DataTable table) {
        List<List<String>> data = table.asLists(String.class);
        List<String> row = data.get(0);

        requestBody = new JSONObject();
        requestBody.put("firstname", row.get(0));
        requestBody.put("lastname", row.get(1));
        requestBody.put("totalprice", row.get(2));
        requestBody.put("depositpaid", row.get(3));

        JSONObject dates = new JSONObject();
        dates.put("checkin", row.get(4));
        dates.put("checkout", row.get(5));

        requestBody.put("bookingdates", dates);
        requestBody.put("additionalneeds", row.get(6));
    }

    @When("the booking is submitted by the user")
    public void the_booking_is_submitted_by_the_user() throws IOException {
        response = Request.Post(String.format("%s/api/booking", BASE_URL))
            .useExpectContinue()
            .setHeader(AuthorizationToken.HEADER_NAME, new AuthorizationToken(BASE_URL).build())
            .bodyString(requestBody.toString(), ContentType.APPLICATION_JSON)
            .execute()
            .returnResponse();
    }

    @Then("the booking is successfully stored")
    public void the_booking_is_successfully_stored() throws IOException {
        Assertions.assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Then("shown to the user as stored")
    public void shown_to_the_user_as_stored() throws IOException {
        String bodyStr = EntityUtils.toString(response.getEntity());
        JSONObject body = new JSONObject(bodyStr);

        Assertions.assertTrue(body.has("id"));
        long bookingId = body.getLong("id");
        Assertions.assertTrue(bookingId >= 1);
    }

    @Given("Hotel Booking has existing bookings")
    public void hotel_booking_has_existing_bookings() throws IOException {
        requestBody = new JSONObject();
        requestBody.put("firstname", "rose");
        requestBody.put("lastname", "boylu");
        requestBody.put("totalprice", 10);
        requestBody.put("depositpaid", true);

        JSONObject dates = new JSONObject();
        dates.put("checkin", "2020-07-24");
        dates.put("checkout", "2020-07-25");

        requestBody.put("bookingdates", dates);
        requestBody.put("additionalneeds", "Nothing");

        response = Request.Post(String.format("%s/api/booking", BASE_URL))
                .useExpectContinue()
                .setHeader(AuthorizationToken.HEADER_NAME, new AuthorizationToken(BASE_URL).build())
                .bodyString(requestBody.toString(), ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();

        Assertions.assertEquals(200, response.getStatusLine().getStatusCode());

        String bodyStr = EntityUtils.toString(response.getEntity());
        JSONObject body = new JSONObject(bodyStr);

        Assertions.assertTrue(body.has("id"));
        long bookingId = body.getLong("id");
        Assertions.assertTrue(bookingId >= 1);
        lastBookingId = bookingId;
    }

    @When("a specific booking is requested by the user")
    public void a_specific_booking_is_requested_by_the_user() throws IOException {
        response = Request.Get(String.format("%s/api/booking/%d", BASE_URL, lastBookingId))
               .useExpectContinue()
               .setHeader(AuthorizationToken.HEADER_NAME, new AuthorizationToken(BASE_URL).build())
               .execute()
               .returnResponse();

        Assertions.assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Then("the booking is shown")
    public void the_booking_is_shown() throws IOException {
        String bodyStr = EntityUtils.toString(response.getEntity());
        JSONObject body = new JSONObject(bodyStr);

        Assertions.assertTrue(body.has("firstname"));
        Assertions.assertTrue(body.has("lastname"));
        Assertions.assertTrue(body.has("totalprice"));
        Assertions.assertTrue(body.has("depositpaid"));
        Assertions.assertTrue(body.has("bookingdates"));
        Assertions.assertTrue(body.has("additionalneeds"));

        JSONObject bookingDates = body.getJSONObject("bookingdates");
        Assertions.assertTrue(bookingDates.has("checkin"));
        Assertions.assertTrue(bookingDates.has("checkout"));
    }

    @When("a specific booking is updated by the user")
    public void a_specific_booking_is_updated_by_the_user() throws IOException {
        requestBody = new JSONObject();
        requestBody.put("firstname", "Matus");
        requestBody.put("lastname", "Novak");
        requestBody.put("totalprice", 30);
        requestBody.put("depositpaid", true);

        JSONObject dates = new JSONObject();
        dates.put("checkin", "2020-07-24");
        dates.put("checkout", "2020-07-25");

        requestBody.put("bookingdates", dates);
        requestBody.put("additionalneeds", "Nothing");

        response = Request.Put(String.format("%s/api/booking/%d", BASE_URL, lastBookingId))
               .useExpectContinue()
               .setHeader(AuthorizationToken.HEADER_NAME, new AuthorizationToken(BASE_URL).build())
               .bodyString(requestBody.toString(), ContentType.APPLICATION_JSON)
               .execute()
               .returnResponse();

        Assertions.assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Then("the booking is shown to be updated")
    public void the_booking_is_shown_to_be_updated() throws IOException {
        String bodyStr = EntityUtils.toString(response.getEntity());
        JSONObject body = new JSONObject(bodyStr);

        Assertions.assertTrue(body.has("firstname"));
        Assertions.assertTrue(body.has("lastname"));

        Assertions.assertEquals("Matus", body.getString("firstname"));
        Assertions.assertEquals("Novak", body.getString("lastname"));
    }

    @When("a specific booking is deleted by the user")
    public void a_specific_booking_is_deleted_by_the_user() throws IOException {
        response = Request.Delete(String.format("%s/api/booking/%d", BASE_URL, lastBookingId))
                .useExpectContinue()
                .setHeader(AuthorizationToken.HEADER_NAME, new AuthorizationToken(BASE_URL).build())
                .execute()
                .returnResponse();

        Assertions.assertEquals(200, response.getStatusLine().getStatusCode());

    }

    @Then("the booking is removed")
    public void the_booking_is_removed() throws IOException {
        response = Request.Get(String.format("%s/api/booking/%d", BASE_URL, lastBookingId))
                .setHeader(AuthorizationToken.HEADER_NAME, new AuthorizationToken(BASE_URL).build())
                .execute()
                .returnResponse();

        Assertions.assertEquals(404, response.getStatusLine().getStatusCode());
    }
}
