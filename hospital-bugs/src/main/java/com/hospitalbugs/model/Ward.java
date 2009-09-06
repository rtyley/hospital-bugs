package com.hospitalbugs.model;

public class Ward {
	private final String wardCode;

	public Ward(String wardCode) {
		this.wardCode = wardCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((wardCode == null) ? 0 : wardCode.hashCode());
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
		Ward other = (Ward) obj;
		if (wardCode == null) {
			if (other.wardCode != null)
				return false;
		} else if (!wardCode.equals(other.wardCode))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+"["+wardCode+"]";
	}
}
