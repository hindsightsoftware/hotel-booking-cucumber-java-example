Feature: Hotel Booking

  @HOTEL-1 @OPEN
  Scenario Outline: Create a booking in Hotel Booking
    Given a user wants to make a booking with the following details
      | Mark | Winters | 120 | true | 2018-01-01 | 2018-01-03 | Breakfast |
    When the booking is submitted by the user
    Then the booking is successfully stored
    And shown to the user as stored

  Scenario: Retrieve a booking in Hotel Booking
    Given Hotel Booking has existing bookings
    When a specific booking is requested by the user
    Then the booking is shown

  Scenario: Delete a booking in Hotel Booking
    Given Hotel Booking has existing bookings
    When a specific booking is deleted by the user
    Then the booking is removed
    And another step
