Feature: New feature

  @HOTEL-14
  Scenario Outline: reward silver members
    Given a booking by a <membership level> member
    And a another step
    
    Examples:
      | membership level | result  |
      | silver           |    pass |