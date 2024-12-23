-- liquibase formatted sql

-- changeset Ryan:1684867215720-1
ALTER TABLE chat_shared_media
    ADD blur_cloud_item_detail_id INTEGER;
ALTER TABLE chat_shared_media
    ADD optimised_cloud_item_detail_id INTEGER;
ALTER TABLE chat_shared_media
    ADD original_cloud_item_detail_id INTEGER;

-- changeset Ryan:1684867215720-2
ALTER TABLE profile_media
    ADD blur_cloud_item_detail_id INTEGER;
ALTER TABLE profile_media
    ADD optimised_cloud_item_detail_id INTEGER;
ALTER TABLE profile_media
    ADD original_cloud_item_detail_id INTEGER;

-- changeset Ryan:1684867215720-3
ALTER TABLE profile_picture
    ADD blur_cloud_item_detail_id INTEGER;
ALTER TABLE profile_picture
    ADD optimised_cloud_item_detail_id INTEGER;
ALTER TABLE profile_picture
    ADD original_cloud_item_detail_id INTEGER;

-- changeset Ryan:1684867215720-10
ALTER TABLE cloud_item_details
    ADD url VARCHAR(255);

-- changeset Ryan:1684867215720-11
ALTER TABLE cloud_item_details
    ALTER COLUMN url SET NOT NULL;

-- changeset Ryan:1684867215720-12
ALTER TABLE chat_shared_media
    ADD CONSTRAINT FK_CHATSHAREDMEDIA_ON_BLUR_CLOUD_ITEM_DETAIL FOREIGN KEY (blur_cloud_item_detail_id) REFERENCES cloud_item_details (id);

-- changeset Ryan:1684867215720-13
ALTER TABLE chat_shared_media
    ADD CONSTRAINT FK_CHATSHAREDMEDIA_ON_OPTIMISED_CLOUD_ITEM_DETAIL FOREIGN KEY (optimised_cloud_item_detail_id) REFERENCES cloud_item_details (id);

-- changeset Ryan:1684867215720-14
ALTER TABLE chat_shared_media
    ADD CONSTRAINT FK_CHATSHAREDMEDIA_ON_ORIGINAL_CLOUD_ITEM_DETAIL FOREIGN KEY (original_cloud_item_detail_id) REFERENCES cloud_item_details (id);

-- changeset Ryan:1684867215720-15
ALTER TABLE profile_media
    ADD CONSTRAINT FK_PROFILEMEDIA_ON_BLUR_CLOUD_ITEM_DETAIL FOREIGN KEY (blur_cloud_item_detail_id) REFERENCES cloud_item_details (id);

-- changeset Ryan:1684867215720-16
ALTER TABLE profile_media
    ADD CONSTRAINT FK_PROFILEMEDIA_ON_OPTIMISED_CLOUD_ITEM_DETAIL FOREIGN KEY (optimised_cloud_item_detail_id) REFERENCES cloud_item_details (id);

-- changeset Ryan:1684867215720-17
ALTER TABLE profile_media
    ADD CONSTRAINT FK_PROFILEMEDIA_ON_ORIGINAL_CLOUD_ITEM_DETAIL FOREIGN KEY (original_cloud_item_detail_id) REFERENCES cloud_item_details (id);

-- changeset Ryan:1684867215720-18
ALTER TABLE profile_picture
    ADD CONSTRAINT FK_PROFILEPICTURE_ON_BLUR_CLOUD_ITEM_DETAIL FOREIGN KEY (blur_cloud_item_detail_id) REFERENCES cloud_item_details (id);

-- changeset Ryan:1684867215720-19
ALTER TABLE profile_picture
    ADD CONSTRAINT FK_PROFILEPICTURE_ON_OPTIMISED_CLOUD_ITEM_DETAIL FOREIGN KEY (optimised_cloud_item_detail_id) REFERENCES cloud_item_details (id);

-- changeset Ryan:1684867215720-20
ALTER TABLE profile_picture
    ADD CONSTRAINT FK_PROFILEPICTURE_ON_ORIGINAL_CLOUD_ITEM_DETAIL FOREIGN KEY (original_cloud_item_detail_id) REFERENCES cloud_item_details (id);

-- changeset Ryan:1684867215720-21
ALTER TABLE chat_shared_media
    DROP COLUMN cloud_item_detail_id;
ALTER TABLE chat_shared_media
    DROP COLUMN url;

-- changeset Ryan:1684867215720-22
ALTER TABLE profile_media
    DROP COLUMN cloud_item_detail_id;
ALTER TABLE profile_media
    DROP COLUMN url;

-- changeset Ryan:1684867215720-23
ALTER TABLE profile_picture
    DROP COLUMN cloud_item_detail_id;
ALTER TABLE profile_picture
    DROP COLUMN url;

