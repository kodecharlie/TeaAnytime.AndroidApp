package com.quisoma.teaanytime;

import java.io.Serializable;

public class Tea implements Serializable
{
	private static final long serialVersionUID = 3220642632610896428L;

	private String name;
	private String group;
	private String imageFileName;
	private String description;
	private int rating;

	public Tea() { }

	public String getName() {
		return name;
	}
	public String getGroup() {
		return group;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public String getDescription() {
		return description;
	}
	public int getRating() {
		return rating;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}

	public Tea copy() {
		Tea retval = new Tea();
		retval.name = this.name;
		retval.group = this.group;
		retval.imageFileName = this.imageFileName;
		retval.description = this.description;
		return retval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((imageFileName == null) ? 0 : imageFileName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tea other = (Tea) obj;
		if (imageFileName == null) {
			if (other.imageFileName != null)
				return false;
		} else if (!imageFileName.equals(other.imageFileName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
