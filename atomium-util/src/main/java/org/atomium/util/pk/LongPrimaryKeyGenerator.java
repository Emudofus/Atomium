package org.atomium.util.pk;

public class LongPrimaryKeyGenerator implements PrimaryKeyGenerator<Long> {

	private Long next;
	
	public void setMax(Long pk) {
		if (next < pk)
			next = pk;
	}

	public Long next() {
		return ++next;
	}

}
