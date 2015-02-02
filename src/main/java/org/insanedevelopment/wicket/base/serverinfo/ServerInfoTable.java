package org.insanedevelopment.wicket.base.serverinfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ServerInfoTable extends DataTable<ServerInfoItem, String> {

	private static final long serialVersionUID = 147545996295074242L;

	public ServerInfoTable(String id) {
		super(id, createColumns(), createDataProvider(), 1000);
	}

	private static List<? extends IColumn<ServerInfoItem, String>> createColumns() {
		List<IColumn<ServerInfoItem, String>> result = new ArrayList<IColumn<ServerInfoItem, String>>();
		result.add(new PropertyColumn<ServerInfoItem, String>(Model.of("Key"), "key"));
		result.add(new PropertyColumn<ServerInfoItem, String>(Model.of("Value"), "value"));
		return result;
	}

	private static IDataProvider<ServerInfoItem> createDataProvider() {
		return new ServerInfoDataProvider();
	}

	private static class ServerInfoDataProvider extends SortableDataProvider<ServerInfoItem, String> {

		private static final long serialVersionUID = 6821085016728500017L;

		private static List<ServerInfoItem> data;

		{
			Map<String, String> environment = System.getenv();
			Properties systemProperties = System.getProperties();
			List<ServerInfoItem> result = new ArrayList<ServerInfoItem>(environment.size() + systemProperties.size() + 16);
			for (Map.Entry<String, String> envItem : environment.entrySet()) {
				result.add(new ServerInfoItem("env-" + envItem.getKey(), envItem.getValue()));
			}

			for (Map.Entry<String, String> envItem : environment.entrySet()) {
				result.add(new ServerInfoItem("env-" + envItem.getKey(), envItem.getValue()));
			}

			for (String propertyKey : systemProperties.stringPropertyNames()) {
				result.add(new ServerInfoItem("sysprop-" + propertyKey, systemProperties.getProperty(propertyKey)));
			}

			try {
				String canonicalHostName = InetAddress.getLocalHost().getCanonicalHostName();
				String hostName = InetAddress.getLocalHost().getHostName();
				result.add(new ServerInfoItem("builtin-full_hostname", canonicalHostName));
				result.add(new ServerInfoItem("builtin-hostname", hostName));
			} catch (UnknownHostException e) {
				System.err.println("Error in getting server info - could not get hostname");
			}
			Collections.sort(result, new Comparator<ServerInfoItem>() {
				@Override
				public int compare(ServerInfoItem o1, ServerInfoItem o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
			});
			data = result;
		}

		@Override
		public Iterator<? extends ServerInfoItem> iterator(long first, long count) {
			if (first > Integer.MAX_VALUE || count > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("first or count is too high");
			}
			return data.subList((int) first, (int) (first + count)).iterator();
		}

		@Override
		public long size() {
			return data.size();
		}

		@Override
		public IModel<ServerInfoItem> model(ServerInfoItem object) {
			return Model.of(object);
		}

	}
}