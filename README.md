# Ticket Controller API

The Ticket Controller API provides endpoints to manage and retrieve ticket-related information.

## Endpoints

### 1. Get Ticket by ID

#### Endpoint
GET /ticket/{id}

#### Description
Retrieve a ticket by its unique ID.

#### Request
- **id** (Path Variable): ID of the ticket.

#### Response
Returns the ticket as a 2D array.


### 2. Get Tickets Grouped by Ticket ID

#### Endpoint
GET /ticket/groupByTicketId/{id}

#### Description
Retrieve a list of tickets grouped by their Ticket ID.

#### Request
- **id** (Path Variable): ID of the ticket.

#### Response
Returns a list of tickets associated with the specified Ticket ID.

### 3. Get All Tickets with Pagination

#### Endpoint
GET /ticket/all

#### Description
Retrieve all tickets with pagination support.

#### Request Parameters
- **page** (Query Parameter, Default: 0): Page number.
- **size** (Query Parameter, Default: 12): Number of tickets per page.

#### Response
Returns a paginated list of tickets.

### 4. Create N Sets of Tickets

#### Endpoint
GET /ticket/create/{N}

#### Description
Generate and create N sets of tickets.

#### Request
- **N** (Path Variable): Number of sets to create.

#### Response
Returns a HashMap with set names as keys and corresponding 2D arrays as values.

## Technologies Used

- Spring Boot
- Java
- Maven
