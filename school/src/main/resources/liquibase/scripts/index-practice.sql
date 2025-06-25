-- liquibase formatted sql

-- changeset vbondarenko:1
CREATE INDEX students_name_index ON student (name);
CREATE INDEX faculty_name_and_color_index ON faculty (name, color);