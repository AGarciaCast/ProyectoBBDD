SELECT COUNT(*) as nVinos
FROM wine;

SELECT *
FROM wine
WHERE title = 'Souverain 2010 Chardonnay (North Coast)' or description= 'Souverain 2010 Chardonnay (North Coast)';


SELECT COUNT(*) as num_val_usr, AVG(wur.score) as avg_usr,wsg.score as val_prof
FROM wine_scoring_guide as wsg
	JOIN wine_user_review as wur on wur.wine_id = wsg.wine_id
WHERE wsg.wine_id=?;

SELECT w1.wine_id, w2.wine_id, W1.title
FROM wine as w1
	JOIN wine as w2 on w1.title=w2.title
WHERE w1.wine_id != w2.wine_id;

SELECT * 
FROM user as u 
	JOIN wine_user_review as wu on u.user_id=wu.user_id
WHERE wu.wine_id=?;

SELECT user_id,name
FROM user
WHERE user_id not in (
	SELECT u.user_id
    FROM user as u 
		Join wine_user_review as ur on ur.user_id=u.user_id
);
SELECT max(num) FROM (SELECT COUNT(*) as num FROM wine_user_review WHERE year(date)=2018 GROUP BY user_id) as sqMax;


SELECT user_id FROM wine_scoring_guide WHERE year(date)=2018 GROUP BY user_id HAVING COUNT(*)=?;

SELECT *
FROM wine_user_review
WHERE user_id=48;

SELECT wy.winery_id, MIN(wy.name) AS name, group_concat(distinct r.name) as regiones, COUNT(distinct c.country_id) as numPaises
FROM winery as wy
	JOIN wine as w on w.winery_id=wy.winery_id
    JOIN region as r on r.region_id = w.region_id
    JOIN country as c on c.country_id= r.country_id
GROUP By wy.winery_id
HAVING numPaises>1;

SELECT MIN(desviacion_media) as minimo
FROM(
SELECT AVG(ABS(wur.score-wsg.score)) AS desviacion_media
FROM wine_user_review as wur
JOIN wine_scoring_guide as wsg on wsg.wine_id = wur.wine_id
GROUP BY wur.user_id) as sqMDesv;

SELECT wur.user_id, MIN(u.name) as name, COUNT(*) as num_vinos, AVG(ABS(wur.score-wsg.score)) AS desviacion_media
FROM wine_user_review as wur
JOIN wine_scoring_guide as wsg on wsg.wine_id = wur.wine_id
JOIN user as u on u.user_id=wur.user_id
GROUP BY wur.user_id
HAVING desviacion_media= 0;

SELECT wine_id, AVG(score) AS media_pro
FROM wine_scoring_guide
GROUP BY wine_id;

SELECT wine_id, score AS media_pro
FROM wine_scoring_guide;



SELECT *
FROM wine 
WHERE title = 'Qui‚vremont 2017 Vin de Maison Red (Virginia)';

INSERT wine(name)
VALUES('PRUEBA');


SELECT region_id
FROM region 
WHERE name = ? and area =? and province =? and country_id = (
SELECT country_id
FROM  country
WHERE name_es=? or name_en=? or name_fr=?);


