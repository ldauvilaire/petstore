package net.ldauvilaire.petstore.test.backend.rest.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;

/**
 * @author Laurent Dauvilaire
 */
public class XmlListWrapper<T> {

	private List<T> items;

	public XmlListWrapper() {
		items = new ArrayList<T>();
	}
	public XmlListWrapper(List<T> items) {
		this.items = items;
	}

	@XmlAnyElement(lax=true)
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
}
