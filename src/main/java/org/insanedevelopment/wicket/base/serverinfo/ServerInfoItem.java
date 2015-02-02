package org.insanedevelopment.wicket.base.serverinfo;

import java.io.Serializable;

public class ServerInfoItem implements Serializable {

	private static final long serialVersionUID = 1740354039340093027L;

	private String key;
	private String value;

	public ServerInfoItem() {

	}

	public ServerInfoItem(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	protected final String getKey() {
		return key;
	}

	protected final void setKey(String key) {
		this.key = key;
	}

	protected final String getValue() {
		return value;
	}

	protected final void setValue(String value) {
		this.value = value;
	}

}
