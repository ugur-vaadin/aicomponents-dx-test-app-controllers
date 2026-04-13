# AI Components DX Test App — Controllers

This is a DX test application for Vaadin AI Components. Participants progressively build AI-powered data views using natural language, with each task building on the previous one.

A non-final version of the documentation can be found at: https://docs-preview-pr-5473.fly.dev/

## Running the Application

To start the application in development mode, import it into your IDE and run the `Application` class.
You can also start the application from the command line by running:

```bash
export OPENAI_API_KEY=your-key-here
or 
export ANTHROPIC_API_KEY=your-key-here
mvn spring-boot:run
```

Then open: http://localhost:8080

## Instructions for Facilitators

1. Have participants open the application
2. Read each task description from the DX test document
3. Participants implement all tasks in `TaskView.java`, building on their previous work

## File Structure

```
src/main/java/com/vaadin/dx/
├── DatabaseHelper.java     # SQL query helper
├── StateStorage.java       # State persistence helper
├── ViewHelper.java         # View utility helper
├── TaskView.java           # Participant workspace
└── solutions/              # Reference implementations
    ├── Solution1View.java  # Grid + DatabaseProvider (/solution1)
    ├── Solution2View.java  # Chart (/solution2)
    ├── Solution3View.java  # State persistence (/solution3)
    └── Solution4View.java  # Custom AIController (/solution4)
```

## Database

The app includes an H2 in-memory database with sample data:

- **employees** — id, name, department, salary, hire_date
- **sales** — id, product, category, region, amount, quantity, sale_date
- **order_hdr** — order_id, cust_name, order_dt, status
- **order_dtl** — id, order_id, product, qty, unit_px
- **temperatures** — id, city, month, avg_temp, min_temp, max_temp
