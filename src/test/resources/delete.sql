delete
from profile_connection;
delete
from profile_match;
delete
from profile_media;
update user_profile
set picture_id = null;
delete
from profile_picture;
delete
from cloud_item_details;
delete
from user_interest;
delete
from user_prompt;
delete
from user_profile;
delete
from users;
delete
from interest;
delete
from interest_category;
delete
from prompt;