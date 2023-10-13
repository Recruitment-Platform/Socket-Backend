ALTER TABLE comment
    DROP group_num,
    DROP group_order,
    DROP comment_class;

ALTER TABLE comment
    ADD COLUMN parent_id BIGINT;

ALTER TABLE comment
    ADD CONSTRAINT fk_comment_parent
        FOREIGN KEY (parent_id)
            REFERENCES comment (comment_id)