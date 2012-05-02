package org.atomium.util.pk;

public class IntegerPrimaryKeyGenerator implements PrimaryKeyGenerator<Integer> {
	
	private Integer next;

	public void setMax(Integer pk) {
		if (next < pk)
			next = pk;
	}

	public Integer next() {
		return ++next;
	}

}
