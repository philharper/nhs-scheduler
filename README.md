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
  - `/leave.html` annual leave and schedule week management
- REST API under `/api`

## Run
1. Ensure Java 21 and Maven are installed.
2. Start app:
   ```bash
   mvn spring-boot:run
   ```
3. Log in with the configured credentials.
   - Local defaults are `admin` / `change-me`
   - Override them with env vars:
     - `SCHEDULER_AUTH_USERNAME`
     - `SCHEDULER_AUTH_PASSWORD`
4. Open:
   - Login page: `http://localhost:8080/login.html`
   - Schedule page: `http://localhost:8080/`
   - Data configuration page: `http://localhost:8080/config.html`
   - Annual leave page: `http://localhost:8080/leave.html`
   - State API: `http://localhost:8080/api/state`

## JSON model
- `rooms[]`: `{ id, name, purpose }`
- `employees[]`: `{ id, name, skills[], availability[] }`
- `employees[].annualLeave[]`: `{ startDate, endDate }`
- `availability[]`: `{ dayOfWeek, start, end }`
- `sessions[]`: `{ id, name, requiredSkill, purpose, dayOfWeek, start, end }`
- `scheduleWeekStart`: Monday date used to map the weekly template to actual calendar dates
- `purposeOptions[]`: configurable purpose values used by room purpose checkboxes
- `skillOptions[]`: configurable skill values used by employee skill checkboxes

Example values for `dayOfWeek`: `MONDAY`, `TUESDAY`, ...
Times use `HH:mm:ss` (for example `08:00:00`).

## API
- `GET /api/state` get current state from file
- `PUT /api/state` replace state and persist to file
- `GET /api/schedule` get the last generated schedule from file
- `POST /api/schedule` generate assignments from current state and persist as the new saved schedule
- `PUT /api/schedule/override` manually override assigned employee for a scheduled session and persist it
- `GET /api/meta` returns the absolute data-file path

## Authentication
- All pages and API routes require login.
- Credentials are configured with:
  - `scheduler.auth.username`
  - `scheduler.auth.password`
- These resolve from environment variables:
  - `SCHEDULER_AUTH_USERNAME`
  - `SCHEDULER_AUTH_PASSWORD`
- Default login page: `/login.html`

## GitHub Secrets
- Store login credentials as repository secrets named:
  - `SCHEDULER_AUTH_USERNAME`
  - `SCHEDULER_AUTH_PASSWORD`
- At deploy/runtime, expose those secrets as environment variables so Spring Boot can read them.

## Notes
- All room, employee, and session fields are editable via the configuration page UI.
- Annual leave is managed on the dedicated leave page and blocks scheduling for the matching calendar dates.
- Room usage is exclusive by time window.
- Employee sessions cannot overlap.
- Room is chosen automatically based on session purpose and room availability.
- Employee must have required skill and availability covering the session window.
- Session duration must be exactly 4 hours.
