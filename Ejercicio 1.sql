
#//////////////////////////////////////////////  CATA DE VINOS /////////////////////////////////////////////////


#///// CREACIÓN DE LA BASE DE DATOS ///// 

CREATE DATABASE cata_vinos DEFAULT CHARACTER SET utf8;


#///// CREACIÓN DE TABLAS //////

CREATE TABLE wine(
	wine_id INT auto_increment,
    name VARCHAR(150),
    title VARCHAR(255),
    description TEXT,
    grape_variety_id INT,
    designation VARCHAR(255),
    winery_id INT,
    region_id INT,
    
    PRIMARY KEY(wine_id)
);

CREATE TABLE grape_variety(
	
    grape_variety_id INT,
	name VARCHAR(150),
    
    PRIMARY KEY(grape_variety_id)
);

CREATE TABLE winery(
	
    winery_id INT,
	name VARCHAR(150),
    
    PRIMARY KEY(winery_id)
);

CREATE TABLE region(
	
    region_id INT,
	name VARCHAR(150),
    area VARCHAR(150),
    province VARCHAR(150),
    country_id INT,
    
    PRIMARY KEY(region_id)
);

CREATE TABLE country(
	
    country_id INT,
	name_es VARCHAR(128),
    name_en VARCHAR(128),
    name_fr VARCHAR(128),
    iso_code_2 VARCHAR(2),
    iso_code_3 VARCHAR(3),
    phone_prefix INT,
    
    PRIMARY KEY(country_id)
);

CREATE TABLE wine_user_review(
	
    wine_id INT,
	user_id INT,
	date DATETIME,
    score decimal(10,2),
    
    PRIMARY KEY(wine_id, user_id)
);

CREATE TABLE user(
	
    user_id INT,
	name VARCHAR(150),
    
    PRIMARY KEY(user_id)
);

CREATE TABLE wine_scoring_guide(
	
    wine_id INT,
	taster_id INT,
	date DATETIME,
    score decimal(10,2),
	price decimal(10,3),
    
    PRIMARY KEY(wine_id)
);

CREATE TABLE taster(
	
	taster_id INT,
	name VARCHAR(150),
    twitter_handle VARCHAR(150),
    
    PRIMARY KEY(taster_id)
);


#///// CREAR FOREIGN KEYS /////

ALTER TABLE wine
ADD CONSTRAINT FK_wine_grape_variety_id FOREIGN KEY(grape_variety_id) REFERENCES grape_variety(grape_variety_id),
ADD CONSTRAINT FK_wine_winery_id FOREIGN KEY(winery_id) REFERENCES winery(winery_id),
ADD CONSTRAINT FK_wine_region_id FOREIGN KEY(region_id) REFERENCES region(region_id)
;

ALTER TABLE region
ADD CONSTRAINT FK_region_country_id FOREIGN KEY(country_id) REFERENCES country(country_id)
;

ALTER TABLE wine_scoring_guide
ADD CONSTRAINT FK_wine_scoring_guide_wine_id FOREIGN KEY(wine_id) REFERENCES wine(wine_id),
ADD CONSTRAINT FK_wine_scoring_guide_taster_id FOREIGN KEY(taster_id) REFERENCES taster(taster_id)
;

ALTER TABLE wine_user_review
ADD CONSTRAINT FK_wine_user_review_wine_id FOREIGN KEY(wine_id) REFERENCES wine(wine_id),
ADD CONSTRAINT FK_wine_user_review_user_id  FOREIGN KEY(user_id) REFERENCES user(user_id)
;



































