--pw decrypted = admin
INSERT INTO user (user_name,email,first_name,last_name,password,role,enabled) VALUES('joel1','hjoel87@gmx.ch','Joel','Henz','$2a$12$JHYEx42z9u3j/Q/AoRMLI.wau3OCmahHhlQWcKf5JfObcKeQxHM7W','ADMIN',true);

--pw decrypted = user
INSERT INTO user (user_name,email,first_name,last_name,password,role,enabled) VALUES('joel2','henzjoel@gmail.com','Joel','Henz','$2a$12$e89TFk53jlBPscfL.YCTZ.8RukYnu66pcjLfaIA2prrtq1xbqcV.i','USER',true);