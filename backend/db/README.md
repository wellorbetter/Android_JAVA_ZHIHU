# Database Migration Plan (MySQL + Flyway)

This project currently runs with a file-backed fallback store for fast local iteration.
The SQL migrations in `backend/db/migrations` define the target production schema.

## Flyway naming
- `V1__init_core_tables.sql`
- `V2__seed_topics.sql`

## Example local run (Docker + Flyway CLI)
1. Start MySQL:
```bash
docker run --name zhihu-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=zhihu -p 3306:3306 -d mysql:8.0
```

2. Run migrations:
```bash
flyway -url=jdbc:mysql://127.0.0.1:3306/zhihu -user=root -password=root -locations=filesystem:backend/db/migrations migrate
```

## Integration roadmap
1. Introduce `JdbcContentRepository` using these tables.
2. Keep file store as local fallback behind repository interface.
3. Add migration check in CI before backend integration tests.
