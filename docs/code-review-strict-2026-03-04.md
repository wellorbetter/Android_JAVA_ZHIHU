# Strict Code Review (2026-03-04)

## Findings and Actions

1. High - JSON escaping correctness risk in backend response builders.
- Scope: `backend/service/ContentService.java`, `backend/service/FeedService.java`
- Risk: Quotes/backslashes/newlines in user content could break JSON payloads.
- Action: Added explicit JSON escaping (`jsonEscape`) and separated TSV escape logic from JSON logic.
- Status: Fixed.

2. High - Comment API lacked pagination contract for scalable lists.
- Scope: `backend/service/ContentService.java`, `backend/http/ContentHandler.java`, Android content DTO/API layer.
- Risk: Large comment sets caused oversized payloads and poor UX.
- Action: Added `cursor + limit` query support, returned `nextCursor/hasMore/totalCount`; Android comments page now supports "load more".
- Status: Fixed.

3. Medium - Comment interaction concurrency race on client side.
- Scope: `app/Activities/CommentsActivity.java`
- Risk: Rapid taps could create duplicate requests and inconsistent UI state.
- Action: Added busy-state guards and button/input enablement control during load/send/delete.
- Status: Fixed.

4. Medium - Home feed page reloaded unnecessarily on every resume.
- Scope: `app/Pages/Home/RecommendedFragment.java`
- Risk: Extra network requests and UI jitter.
- Action: Added first-load gating (`hasLoadedOnce`) and event-driven refresh path.
- Status: Fixed.

5. Low - Comment capability was discoverable only via modal flow.
- Scope: Android comments UX.
- Action: Introduced dedicated `CommentsActivity` with pull-to-refresh and explicit actions.
- Status: Fixed.

## Residual Risks (not fully closed)

1. Backend persistence still file-backed (`TSV`) for runtime data.
- Impact: Limited concurrency guarantees and no query indexing.
- Next: Replace runtime store with JDBC repository on MySQL.

2. Backend JSON parsing for some request bodies remains regex-based.
- Impact: Fragile under edge-case payloads.
- Next: Migrate to structured JSON parser in backend module.

3. Android end-to-end build/test not fully validated in this environment due Gradle/network constraints.
- Impact: Runtime regressions still possible in UI layer.
- Next: run full `./gradlew assembleDebug testDebugUnitTest` in local/dev CI.
