-- Insert into course_category table
INSERT INTO course_category (category_name)
SELECT 'Web Development' WHERE NOT EXISTS (SELECT 1 FROM course_category WHERE category_name = 'Web Development');

INSERT INTO course_category (category_name)
SELECT 'Mobile Development' WHERE NOT EXISTS (SELECT 1 FROM course_category WHERE category_name = 'Mobile Development');

INSERT INTO course_category (category_name)
SELECT 'Data Science' WHERE NOT EXISTS (SELECT 1 FROM course_category WHERE category_name = 'Data Science');

INSERT INTO course_category (category_name)
SELECT 'Cloud Computing' WHERE NOT EXISTS (SELECT 1 FROM course_category WHERE category_name = 'Cloud Computing');

INSERT INTO course_category (category_name)
SELECT 'Software Testing' WHERE NOT EXISTS (SELECT 1 FROM course_category WHERE category_name = 'Software Testing');

INSERT INTO course_category (category_name)
SELECT 'Project Management' WHERE NOT EXISTS (SELECT 1 FROM course_category WHERE category_name = 'Project Management');

-- Insert into course table
INSERT INTO course (course_title, course_content, category_id)
SELECT 'Introduction to HTML and CSS', 'HTML and CSS are the foundational technologies of web development.', 1
    WHERE NOT EXISTS (SELECT 1 FROM course WHERE course_title = 'Introduction to HTML and CSS');

INSERT INTO course (course_title, course_content, category_id)
SELECT 'Introduction to Python', 'Python is a high-level programming language designed to be easy.', 3
    WHERE NOT EXISTS (SELECT 1 FROM course WHERE course_title = 'Introduction to Python');

INSERT INTO course (course_title, course_content, category_id)
SELECT 'Building Android Apps', 'This course teaches you how to build mobile applications for Android devices.', 2
    WHERE NOT EXISTS (SELECT 1 FROM course WHERE course_title = 'Building Android Apps');

INSERT INTO course (course_title, course_content, category_id)
SELECT 'JavaScript: From Zero to Hero', 'This course teaches you the basics of JavaScript and its practical applications.', 1
    WHERE NOT EXISTS (SELECT 1 FROM course WHERE course_title = 'JavaScript: From Zero to Hero');

INSERT INTO course (course_title, course_content, category_id)
SELECT 'AWS Cloud Computing', 'AWS is a subsidiary of Amazon that provides on-demand cloud computing platforms.', 4
    WHERE NOT EXISTS (SELECT 1 FROM course WHERE course_title = 'AWS Cloud Computing');
