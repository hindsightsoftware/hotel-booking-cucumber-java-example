@loyalty
Feature: Reward frequent customers

  Scenario: Gold member checks in and a superior room is available
    Given a gold loyalty guest with a standard room reservation
    And there are superior rooms available for the length of their stay
    When the guest checks in
    Then the guest should be upgraded to a superior room