CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL,
  mobile VARCHAR(32) DEFAULT '',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_users_username (username)
);

CREATE TABLE IF NOT EXISTS topics (
  topic_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  topic_name VARCHAR(32) NOT NULL,
  description VARCHAR(255) NOT NULL DEFAULT '',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_topics_name (topic_name)
);

CREATE TABLE IF NOT EXISTS content_posts (
  content_id VARCHAR(64) PRIMARY KEY,
  user_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  summary VARCHAR(1000) NOT NULL,
  topic_name VARCHAR(32) NOT NULL,
  like_count INT NOT NULL DEFAULT 0,
  comment_count INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_posts_topic_created (topic_name, created_at),
  CONSTRAINT fk_posts_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS content_likes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  content_id VARCHAR(64) NOT NULL,
  user_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_likes_content_user (content_id, user_id),
  KEY idx_likes_content (content_id),
  CONSTRAINT fk_likes_content_id FOREIGN KEY (content_id) REFERENCES content_posts(content_id),
  CONSTRAINT fk_likes_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS crawler_seed_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  source VARCHAR(64) NOT NULL,
  title VARCHAR(255) NOT NULL,
  url VARCHAR(1024) NOT NULL,
  published_at VARCHAR(64) DEFAULT '',
  summary VARCHAR(1000) NOT NULL,
  topic_name VARCHAR(32) NOT NULL,
  author_name VARCHAR(64) NOT NULL,
  fetched_at BIGINT NOT NULL,
  UNIQUE KEY uk_seed_url (url(255)),
  KEY idx_seed_topic_fetched (topic_name, fetched_at)
);
