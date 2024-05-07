# Cinema Room REST Service
This project aimed to develope simple REST service for managing a cinema room. This project is part of the [Hyperskill training course](https://hyperskill.org/projects/133).

# API endpoints
## Get Avaible Seat
  - Endpoint: GET /seats
  - Desription: Get info about all seats in cinema room
  - Response: Returns a JSON object containing the number of rows and columns in cinema room and a list of all seats
  - Example:
    ```json
    {
      "total_rows": 9,
      "total_columns": 9,
      "seats": [
          {
            "row": 1,
            "column": 1,
            "price": 10
          },
          {
            "row": 1,
            "column": 2,
            "price": 10
          },
          {
            "row": 1,
            "column": 3,
            "price": 10
          },
          ...
          {
            "row": 9,
            "column": 9,
            "price": 8
          }
      ]
    }
    ```
## Purchase Ticket
  - Endpoint: POST /purchase
  - Desription: Buy a ticket for a specific place
  - Request: Requires a JSON object containing a row and column number of a seat
  - Response: Returns a JSON object representing a purchased ticket if the row and column number are correct and the seat has not been purchased
  - Example:
    - Correct row and column number
    ```json
    request
    {
      "row": 1,
      "column": 1
    }
    ```
    ```json
    response
    {
      "token": "409548d1-2f6b-4180-8f70-5800c77c17a8",
      "ticket": {
          "row": 1,
          "column": 1,
          "price": 10
      }
    }
    ```
    - Wrong row and column number
    ```json
    request
    {
      "row": 15,
      "column": 1
    }
    ```
    ```json
    response
    {
      "error": "The number of a row or a column is out of bounds!"
    }
    ```
    - The seat has already been purchased
    ```json
    request
    {
      "row": 1,
      "column": 1
    }
    ```
    ```json
    response
    {
      "error": "The ticket has been already purchased!"
    }
    ```
## Return Ticket
  - Endpoint: POST /return
  - Desription: Return a previously purchased ticket
  - Request: Requires a JSON object containing the ticket token
  - Response: Returns a JSON object representing the returned ticket if the token is valid
  - Example:
    - Correct token
    ```json
    request
    {
      "token": "409548d1-2f6b-4180-8f70-5800c77c17a8"
    }
    ```
    ```json
    response
    {
      "ticket": {
          "row": 1,
          "column": 1,
          "price": 10
      }
    }
    ```
    - Wrong token
    ```json
    request
    {
      "token": "512548d1-2f6b-4180-8f70-5800c77c23a8"
    }
    ```
    ```json
    response
    {
      "error": "Wrong token!"
    }
    ```
## Get Stats
  - Endpoint: GET /stats
  - Desription: Get current income and the number of available and purchased tickets
  - Request: Requires ADMIN authority
  - Response: Returns a JSON object containing the cinema room statistics
  - Example:
  ```json
  {
    "income": 0,
    "available": 81,
    "purchased": 0
  }
