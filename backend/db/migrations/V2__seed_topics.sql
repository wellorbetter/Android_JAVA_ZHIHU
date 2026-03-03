INSERT INTO topics (topic_name, description)
VALUES
('Android', 'Android 工程实践'),
('iOS', 'iOS 架构与性能'),
('Web', 'Web 全栈工程'),
('Backend', '后端架构与服务治理'),
('AI', 'LLM 与应用开发')
ON DUPLICATE KEY UPDATE description = VALUES(description);
