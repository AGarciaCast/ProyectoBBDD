package es.upm.etsiinf.bbdd.proyecto.conexion;

public class Wine {
	private int wine_id,grape_variety_id,winery_id,region_id;
	private String name, title, designation, description;
	public Wine(int wine_id, int grape_variety_id, int winery_id, int region_id, String name, String title,
			String designation, String description) {
		this.wine_id = wine_id;
		this.grape_variety_id = grape_variety_id;
		this.winery_id = winery_id;
		this.region_id = region_id;
		this.name = name;
		this.title = title;
		this.designation = designation;
		this.description = description;
	}
	public int getGrape_variety_id() {
		return grape_variety_id;
	}
	public void setGrape_variety_id(int grape_variety_id) {
		this.grape_variety_id = grape_variety_id;
	}
	public int getWinery_id() {
		return winery_id;
	}
	public void setWinery_id(int winery_id) {
		this.winery_id = winery_id;
	}
	public int getRegion_id() {
		return region_id;
	}
	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	//No tiene setter Wine id al ser PK
	public int getWine_id() {
		return wine_id;
	}
	@Override
	public String toString() {
		return "Wine [wine_id=" + wine_id + ", grape_variety_id=" + grape_variety_id + ", winery_id=" + winery_id
				+ ", region_id=" + region_id + ", name=" + name + ", title=" + title + ", designation=" + designation
				+ ", description=" + description + "]";
	}
	
	
}
