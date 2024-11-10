# I. Getting Started
Starting CLI Application:
- Entry point is at App.java (_src/main/java/com/carrentalproj/App.java_)

Prerequisites:
- Execute SQL Script (_SQL_Script/Create_DB_Tables.sql_) to create required database and tables
- Modify DatabaseConnection's constructor (_src/main/java/com/carrentalproj/databaseconnection/DatabaseConnection.java_) to include mysql server credentials

# II. Conceptual Data Model
![ConceptualDataModel_Overview](https://github.com/user-attachments/assets/8f9b5249-e5f3-4885-ae5d-29cc18b47025)

The conceptual data model provides an overview of the relationships between entities within the Car Rental Application. It illustrates how each entity interacts and connects, forming the foundation for the application's data structure and functionality.

# III. Client
## Finite-State Machine
![ConceptualDataModel_ClientFiniteStateMachine](https://github.com/user-attachments/assets/094b060f-5d05-4746-94d8-ed1010ba4d86)

The client operates as a finite-state machine, where the _**Client Context**_ maintains all relevant information needed to perform tasks on the client, including its current state. Meanwhile, the _**Client State**_ encapsulates the state-specific logic required to process actions and transition to subsequent states.

## State Transition Diagram
![ConceptualDataModel_StateTransitionDiagram](https://github.com/user-attachments/assets/9ce0f5c2-f8df-409d-9338-751397abf2df)

# IV. Sequence Diagram
![ConceptualDataModel_SequenceDiagram](https://github.com/user-attachments/assets/721a292a-df7f-469b-921a-ca564e4ac38f)
