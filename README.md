# NHS Cardiology Diagnostics Scheduler (Spring Boot)

A scheduling app for NHS cardiology diagnostic departments.

## What it does
- Stores rooms with a fixed clinical purpose.
- Stores employees with skills and weekly availability windows.
- Stores diagnostic sessions (no room preselected).
- Enforces each session as a half-day block (4 hours exactly).
- Generates a schedule that assigns each session to an appropriate room and available, qualified employee.
- Marks sessions as unscheduled when constraints cannot be met.
- Persists all state to a shareable JSON file: `data/schedule-state.json`.

## Tech
- Java 21
- Spring Boot 3
- Two-page HTML UI:
  - `/` schedule calendar
  - `/config.html` full data configuration
- REST API under `/api`

## Run
1. Ensure Java 21 and Maven are installed.
2. Start app:
   ```bash
   mvn spring-boot:run
   ```
3. Open:
   - Schedule page: `http://localhost:8080/`
   - Data configuration page: `http://localhost:8080/config.html`
   - State API: `http://localhost:8080/api/state`

## JSON model
- `rooms[]`: `{ id, name, purpose }`
- `employees[]`: `{ id, name, skills[], availability[] }`
- `availability[]`: `{ dayOfWeek, start, end }`
- `sessions[]`: `{ id, name, requiredSkill, purpose, dayOfWeek, start, end }`

Example values for `dayOfWeek`: `MONDAY`, `TUESDAY`, ...
Times use `HH:mm:ss` (for example `08:00:00`).

## API
- `GET /api/state` get current state from file
- `PUT /api/state` replace state and persist to file
- `POST /api/schedule` generate assignments from current state
- `GET /api/meta` returns the absolute data-file path

## Notes
- All room, employee, and session fields are editable via the configuration page UI.
- Room usage is exclusive by time window.
- Employee sessions cannot overlap.
- Room is chosen automatically based on session purpose and room availability.
- Employee must have required skill and availability covering the session window.
- Session duration must be exactly 4 hours.
