-- 1  Возраст студента не может быть меньше 16 лет.
ALTER TABLE student ADD CONSTRAINT age CHECK (age>=16);
-- 2  Имена студентов должны быть уникальными и не равны нулю.
ALTER TABLE student ADD CONSTRAINT name UNIQUE(name);
-- 3 Пара “значение названия” - “цвет факультета” должна быть уникальной.
ALTER TABLE student ADD CONSTRAINT name CHECK (name <>NULL AND name <>'');
ALTER TABLE faculty ADD CONSTRAINT name,color UNIQUE(name,color);
-- 4  При создании студента без возраста ему автоматически должно присваиваться 20 лет.
ALTER TABLE student ALTER COLUMN SET DEFAULT 20;