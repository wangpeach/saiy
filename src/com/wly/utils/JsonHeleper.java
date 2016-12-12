package com.wly.utils;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 解析json数据帮助类
 * 
 * @author ant
 * 
 */
public class JsonHeleper {

	// public static String dateFormat = "yyyy-MM-dd HH:mm";
	public static String dateFormat = "yyyy-MM-dd";
	public static String modelPackage = "com.wly.models";

	/**
	 * 将json数据转化为map类型
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, String> toMap(String jsonStr) {
		String key = "";
		String value = "";
		Map<String, String> map = new HashMap<String, String>();
		JSONObject jsonObject;
		jsonObject = JSONObject.fromObject(jsonStr);
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = jsonObject.keys();
		while (iterator.hasNext()) {
			key = iterator.next();
			value = jsonObject.getString(key);
			map.put(key, value);
		}
		return map;
	}
	
	
	/**
	 * 将一个实体类转换为Map对象
	 * 
	 * @param arg
	 * @return
	 */
	public static Map<String, Object> toMap(Object arg) {
		Map<String, Object> other = new HashMap<String, Object>();
		try {
			Field[] fields = arg.getClass().getDeclaredFields();
			Method[] methods = arg.getClass().getDeclaredMethods();
			for (Method item : methods) {
				String methodName = item.getName();
				if (methodName.startsWith("get")) {
					int inx = -1;
					boolean propex = false;
					String temp = methodName.substring(methodName.indexOf("get") + 3, methodName.length())
							.toLowerCase();
					for (int i = 0; i < fields.length; i++) {
						if (temp.equals(fields[i].getName().toLowerCase())) {
							inx = i;
							propex = true;
							break;
						}
					}
					if (propex) {
						Object value = item.invoke(arg, new Object[] {});
						other.put(fields[inx].getName(), value != null ? value.toString() : "");
					}
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return other;
	}

	/**
	 * 将json数据转换为相应的实体对象
	 * 
	 * @param
	 * @param jsonStr
	 * @return
	 * @throws JSONException
	 */
	public static Object toJavaBean(Object clazz, String jsonStr) {
		Method[] methods = clazz.getClass().getDeclaredMethods();
		Map<String, String> map = toMap(jsonStr);
		for (Method item : methods) {
			if (item.getName().startsWith("set")) {
				String property = item.getName();
				property = property.substring(property.indexOf("set") + 3);
				property = property.toLowerCase().charAt(0)
						+ property.substring(1);
				Class<?> _fieldClass = null;
				try {
					_fieldClass = clazz.getClass().getDeclaredField(property).getType();
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					if (map.get(property) != null) {
						if(Class.forName("java.lang.Integer") == _fieldClass) {
							item.invoke(clazz, new Object[] { Integer.parseInt(map.get(property)) });
						} else if(Class.forName("java.lang.String") == _fieldClass) {
							item.invoke(clazz, new Object[] { map.get(property) });
						} else if(Class.forName("java.util.Date") == _fieldClass) {
							item.invoke(clazz, new Object[] { new SimpleDateFormat().parse(map.get(property))  });
						} 
					} else {
//						item.invoke(clazz, new Object[] { "" });
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return clazz;
	}
	
	/**
	 * 返回json格式数据
	 * 
	 * @param rs
	 * @param
	 * @return 格式 ({State:arg, jsonName:[[],[]]})
	 */
	public static String toJson(String state, ResultSet rs) {
		int rowCount = 0;
		int colCount = 0;
		int rowInx = 0;
		StringBuilder sbr = new StringBuilder();
		if (rs == null) {
			return JsonHeleper.toJson("no", "没有找到数据");
		}
		try {
			if (rs.wasNull()) {
				return JsonHeleper.toJson("no", "没有找到数据");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			rs.last();
			if (rs.getRow() == 0) {
				return JsonHeleper.toJson("no", "没有找到数据");
			}
			rs.beforeFirst();
			// 获取元数据信息
			java.sql.ResultSetMetaData md = rs.getMetaData();
			// 行数
			colCount = md.getColumnCount();

			sbr.append("{\"" + "State" + "\":\"" + state + "\",");
			sbr.append("\"data\":[");
			// 获取行总数
			if (rs.last()) {
				rowCount = rs.getRow();
				// 将指针移至起始位置
				rs.beforeFirst();
			}
			if (rs != null && rowCount > 0) {
				sbr.append("[");
				for (int i = 1; i <= colCount; i++) {
					// 列名
					sbr.append("\"" + md.getColumnLabel(i) + "\"");
					if (i < colCount) {
						sbr.append(",");
					}
				}
				sbr.append("]");
				sbr.append(",");
				while (rs.next()) {
					sbr.append("[");
					for (int j = 1; j <= colCount; j++) {
						sbr.append("\"" + rs.getString(j) + "\"");
						if (j < colCount) {
							sbr.append(",");
						}
					}
					sbr.append("]");
					sbr.append(",");
				}
				sbr.setCharAt(sbr.lastIndexOf(","), ' ');
				sbr.append("]}");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sbr.toString();
	}

	/**
	 * 转换map为json数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String toJson(Map<String, Object> map) {
		StringBuilder sbr = new StringBuilder();
		Set<String> set = map.keySet();
		if (set != null && set.size() > 0) {
			Iterator<String> iterator = set.iterator();
			sbr.append("{");
			while (iterator.hasNext()) {
				String type = (String) iterator.next();
				sbr.append("\"" + type + "\":");
				if (map.get(type) instanceof ArrayList) {
					sbr.append(wrap(((ArrayList<Object>) map.get(type))));
				} else if (checkModel(map.get(type))) {
					sbr.append(wrap(map.get(type)));
				} else if (map.get(type) instanceof Date) {
					SimpleDateFormat format = new SimpleDateFormat(dateFormat);
					String value = format.format(map.get(type));
					sbr.append("\"" + value + "\"");
				} else {
					sbr.append("\"" + Utils.charEscape(map.get(type).toString()) + "\"");
				}
				sbr.append(",");
			}
			sbr.setCharAt(sbr.lastIndexOf(","), ' ');
			sbr.append("}");
		}
		return sbr.toString();
	}

	/**
	 * 转换map类型的集合为json数据
	 * 
	 * @param status
	 * @param maps
	 * @return
	 */
	public static String toJsonForMap(String status, List<Map<String, Object>> maps) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{\"state\": ");
		sbr.append("\"" + status + "\",");
		sbr.append("\"data\": ");
		sbr.append("[");
		if (maps != null && maps.size() > 0) {
			for (Map<String, Object> other : maps) {
				sbr.append(toJson(other));
				sbr.append(",");
			}
			sbr.setCharAt(sbr.lastIndexOf(","), ' ');
		}
		sbr.append("]");
		sbr.append("}");
		return sbr.toString();
	}
	
	public static String toJsonForMap(Map<String, Object> arg, List<Map<String, Object>> maps) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{\"res\": ");
		sbr.append("" + JsonHeleper.toJson(arg) + ",");
		sbr.append("\"data\": ");
		sbr.append("[");
		if (maps != null && maps.size() > 0) {
			for (Map<String, Object> other : maps) {
				sbr.append(toJson(other));
				sbr.append(",");
			}
			sbr.setCharAt(sbr.lastIndexOf(","), ' ');
		}
		sbr.append("]");
		sbr.append("}");
		return sbr.toString();
	}

	/**
	 * 
	 * @param status
	 * @param keys
	 * @param values
	 * @format {state: "", data: [{},{}...]}
	 * @return
	 */
	public static String toJson(String status, String[] keys, List<String[]> values) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{\"state\": ");
		if (status == null || status.equals("")) {
			throw new NullPointerException("返回状态?不能为空");
		} else {
			sbr.append("\"" + status + "\",");
		}
		sbr.append("\"data\": ");
		sbr.append("[");
		if (values != null && values.size() > 0) {
			for (int i = 0; i < values.size(); i++) {

				String[] value = values.get(i);
				if (keys.length != value.length) {
					try {
						throw new Exception("键与值长度不匹配");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					sbr.append("{");
					for (int j = 0; j < keys.length; j++) {
						sbr.append("\"" + keys[j] + "\":");
						sbr.append("\"" + value[j] + "\"");
						if (j < keys.length - 1) {
							sbr.append(",");
						}
					}
					sbr.append("}");
				}
				if (i < values.size() - 1) {
					sbr.append(",");
				}
			}
		}

		sbr.append("]");
		sbr.append("}");
		return sbr.toString();
	}

	/**
	 * @param status
	 * @param clazz
	 * @return
	 */
	public static String toJson(String status, Object clazz) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{\"state\": ");
		sbr.append("\"" + status + "\",");
		sbr.append("\"data\": ");
		sbr.append("{");
		// 获取类中以声明的方法
		Method[] methods = clazz.getClass().getDeclaredMethods();
		// int methodLen = methods.length;
		// int curMetInx = 0;
		for (Method item : methods) {
			String property = item.getName();
			// 如果方法名是以get开始
			if (property.startsWith("get")) {
				String field = property.substring(property.indexOf("get") + 3, property.length()).toLowerCase();
				sbr.append("\"" + field + "\":");
				try {
					Object value = item.invoke(clazz, new Object[] {});
					if (value != null) {
						if(value instanceof ArrayList) {
							sbr.append(wrap(((ArrayList<Object>)value)));
						} else if(checkModel(value)) {
							 sbr.append(wrap(value));
						} else if(value instanceof Date) {
							SimpleDateFormat format = new SimpleDateFormat(dateFormat);
							value = format.format(value);
							sbr.append("\"" + value + "\"");
						} else{
							sbr.append("\"" + value + "\"");
						}
					} else {
						sbr.append("\"\"");
					}
					sbr.append(",");
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		sbr.setCharAt(sbr.lastIndexOf(","), ' ');
		sbr.append("}");
		sbr.append("}");
		return sbr.toString();
	}

	/**
	 * 解析对象集合成json对象， 可无限级嵌套集合 可包含实体类 解析实体类请注意配置 modelPackage 属性， 值为实体类的包名
	 * 
	 * @param status
	 * @param clazzs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String toJson(String status, List<?> clazzs) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("[");
		int clazzLen = clazzs.size();
		int curClzInx = 0;
		for (Object itemSender : clazzs) {
			sbr.append("{");
			// 获取类中以声明的方法
			Method[] methods = itemSender.getClass().getDeclaredMethods();
			// int methodLen = methods.length;
			// int curMetInx = 0;
			for (Method item : methods) {
				String property = item.getName();
				// 如果方法名是以get开始
				if (property.startsWith("get")) {
					String field = property.substring(property.indexOf("get") + 3, property.length()).toLowerCase();
					sbr.append("\"" + field + "\":");
					try {
						Object value = item.invoke(itemSender, new Object[] {});
						if (value != null) {
							if (value instanceof ArrayList) {
								sbr.append(wrap(((ArrayList<Object>) value)));
							} else if (checkModel(value)) {
								sbr.append(wrap(value));
							} else if (value instanceof Date) {
								SimpleDateFormat format = new SimpleDateFormat(dateFormat);
								value = format.format(value);
								sbr.append("\"" + value + "\"");
							} else {
								sbr.append("\"" + Utils.charEscape(value.toString()) + "\"");
							}
						} else {
							sbr.append("\"\"");
						}
						sbr.append(",");
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			sbr.setCharAt(sbr.lastIndexOf(","), ' ');
			sbr.append("}");
			if (curClzInx < clazzLen - 1) {
				sbr.append(",");
			}
			curClzInx++;
		}
		sbr.append("]");
		if (status == null || status.equals("")) {
			return sbr.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"state\": ");
			sb.append("\"" + status + "\",");
			sb.append("\"data\": ");
			sb.append(sbr.toString());
			sb.append("}");
			return sb.toString();
		}
	}

	private static boolean checkModel(Object sender) {
		boolean result = false;
		String name = sender.getClass().getName();
		if (name != null && name.trim().length() > 0) {
			if (name.contains(modelPackage)) {
				result = true;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static String wrap(Object wrap) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{");
		// 获取类中以声明的方法
		Method[] methods = wrap.getClass().getDeclaredMethods();
		// int methodLen = methods.length;
		// int curMetInx = 0;
		for (Method other : methods) {
			String property = other.getName();
			// 如果方法名是以get开始
			if (property.startsWith("get")) {
				String field = property.substring(property.indexOf("get") + 3, property.length()).toLowerCase();
				sbr.append("\"" + field + "\":");
				try {
					Object value = other.invoke(wrap, new Object[] {});
					if (value != null) {
						if (value instanceof ArrayList) {
							sbr.append(wrap((ArrayList<Object>) value));
						} else if (checkModel(value)) {
							sbr.append(wrap(value));
						} else if (value instanceof Date) {
							SimpleDateFormat format = new SimpleDateFormat(dateFormat);
							value = format.format(value);
						} else {
							sbr.append("\"" + Utils.charEscape(value.toString()) + "\"");
						}
					} else {
						sbr.append("\"\"");
					}
					sbr.append(",");
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		sbr.setCharAt(sbr.lastIndexOf(","), ' ');
		sbr.append("}");
		return sbr.toString();
	}

	@SuppressWarnings("unchecked")
	private static String wrap(List<Object> wrap) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("[");
		int size = wrap.size();
		int curClzInx = 0;
		for (Object item : wrap) {
			if (item instanceof String) {
				sbr.append("\"" + Utils.charEscape(item.toString()) + "\"");
			} else if(item instanceof HashMap) {
				sbr.append(toJson((HashMap)item));
			} else {
				sbr.append("{");
				// 获取类中以声明的方法
				Method[] methods = item.getClass().getDeclaredMethods();
				// int methodLen = methods.length;
				// int curMetInx = 0;
				for (Method other : methods) {
					String property = other.getName();
					// 如果方法名是以get开始
					if (property.startsWith("get")) {
						String field = property.substring(property.indexOf("get") + 3, property.length()).toLowerCase();
						sbr.append("\"" + field + "\":");
						try {
							Object[] params = {new Object()};
							Object value = other.invoke(item, params);
							if (value != null) {
								if (value instanceof ArrayList) {
									sbr.append(wrap((ArrayList<Object>) value));
								} else if (checkModel(value)) {
									sbr.append(wrap(value));
								} else if (value instanceof Date) {
									SimpleDateFormat format = new SimpleDateFormat(dateFormat);
									value = format.format(value);
								} else {
									sbr.append("\"" + Utils.charEscape(value.toString()) + "\"");
								}
							} else {
								sbr.append("\"\"");
							}
							sbr.append(",");
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				sbr.setCharAt(sbr.lastIndexOf(","), ' ');
				sbr.append("}");
			}
			
			if (curClzInx < size - 1) {
				sbr.append(",");
			}
			curClzInx++;
		}
		sbr.append("]");

		return sbr.toString();
	}

	/**
	 * 把javabean转换成json对象，bean存在父子关系时调用
	 * 
	 * @param status
	 * @param clazzs
	 * @return
	 */
	public static String toJsonByBeanIncludeSuper(String status, List<Object> clazzs) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{\"state\": ");
		if (status == null || status.equals("")) {
			throw new NullPointerException("返回状�?不能为空");
		} else {
			sbr.append("\"" + status + "\",");
		}
		sbr.append("\"data\": ");
		sbr.append("[");
		int clazzLen = clazzs.size();
		int curClzInx = 0;
		for (Object clazz : clazzs) {
			sbr.append("{");
			Field[] fields = clazz.getClass().getDeclaredFields();
			Field[] superFields = clazz.getClass().getSuperclass().getDeclaredFields();
			sbr.append(getBeanDataIncSup(superFields, clazz));
			sbr.append(",");
			sbr.append(getBeanDataIncSup(fields, clazz));
			sbr.append("}");
			if (curClzInx < clazzLen - 1) {
				sbr.append(",");
			}
			curClzInx++;
		}
		sbr.append("]");
		sbr.append("}");
		return sbr.toString();
	}

	private static String getBeanDataIncSup(Field[] fields, Object clazz) {
		StringBuilder sbr = new StringBuilder();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			sbr.append("\"" + fieldName + "\":");
			try {
				boolean accessFlag = fields[i].isAccessible();
				fields[i].setAccessible(true);
				Object value = fields[i].get(clazz);
				if (value != null) {
					sbr.append("\"" + value + "\"");
				} else {
					sbr.append("\"\"");
				}
				fields[i].setAccessible(accessFlag);
				sbr.append(",");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sbr.setCharAt(sbr.lastIndexOf(","), ' ');
		return sbr.toString();
	}

	public static String toJson(String status, String data) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{");
		sbr.append("\"");
		sbr.append("reason");
		sbr.append("\"");
		sbr.append(":");
		sbr.append("\"");
		sbr.append("" + data + "");
		sbr.append("\",");
		sbr.append("\"");
		sbr.append("state");
		sbr.append("\":");
		sbr.append("\"");
		sbr.append("" + status + "");
		sbr.append("\"");
		sbr.append("}");
		return sbr.toString();
	}


	// {State: yes || no, Data:[[],[],[]....] || data not find}
	public static String toJson(List<List<Object>> data) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{\"State\":");
		if (data == null) {
			sbr.append("\"no\",");
			sbr.append("\"Data\":\"data not found\"");
		}
		if (data.size() == 0) {
			sbr.append("\"no\",");
			sbr.append("\"Data\":\"data not found\"");
		} else {
			sbr.append("\"yes\",");
			sbr.append("\"Data\":");
			sbr.append("[");
			for (int i = 0; i < data.size(); i++) {
				sbr.append("[");
				for (int j = 0; j < data.get(i).size(); j++) {
					sbr.append("\"" + data.get(i).get(j).toString() + "\"");
					if (j < data.get(i).size() - 1) {
						sbr.append(",");
					}
				}
				sbr.append("]");
				if (i < data.size() - 1) {
					sbr.append(",");
				}
			}
			sbr.append("]");
		}
		sbr.append("}");
		return sbr.toString();
	}

	public final static String generateTreeNode(String id, String pId, String name) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{");
		sbr.append("\"id\":");
		sbr.append("\"" + id + "\"");
		sbr.append(",");
		sbr.append("\"pId\":");
		sbr.append("\"" + pId + "\"");
		sbr.append(",");
		sbr.append("\"name\":");
		sbr.append("\"" + name + "\"");
		sbr.append("},");
		return sbr.toString();
	}

	// 生成树方�?
	public final static String generateTreeNode(String id, String pId, String name, boolean isCheck) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{");
		sbr.append("\"id\":");
		sbr.append("\"" + id + "\"");
		sbr.append(",");
		sbr.append("\"pId\":");
		sbr.append("\"" + pId + "\"");
		sbr.append(",");
		sbr.append("\"name\":");
		sbr.append("\"" + name + "\"");
		if (isCheck == true) {
			sbr.append(",");
			sbr.append("checked:true");
		}
		sbr.append("},");
		return sbr.toString();
	}

	public static String generateTreeNode(String id, String pId, String name, String uri, String iden,
			boolean isCheck) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{");
		sbr.append("\"id\":");
		sbr.append("\"" + id + "\"");
		sbr.append(",");
		sbr.append("\"pId\":");
		sbr.append("\"" + pId + "\"");
		sbr.append(",");
		sbr.append("\"name\":");
		sbr.append("\"" + name + "\"");
		if (uri != null && uri != "" && uri != "null") {
			sbr.append(",");
			sbr.append("\"uri\":");
			sbr.append("\"" + uri + "\"");
		}
		if (iden != null && iden != "" && iden != "null") {
			sbr.append(",");
			sbr.append("\"iden\":");
			sbr.append("\"" + iden + "\"");
		}
		if (isCheck == true) {
			sbr.append(",");
			sbr.append("checked:true");
		}
		sbr.append("},");
		return sbr.toString();
	}

	/**
	 * 异步加载
	 * 
	 * @param id
	 * @param pId
	 * @param name
	 * @param isParent
	 *            元素是否时父节点
	 * @return
	 */
	public static String generateTreeNodeForBigData(String id, String pId, String name, boolean isParent) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{");
		sbr.append("\"id\":");
		sbr.append("\"" + id + "\"");
		sbr.append(",");
		sbr.append("\"pId\":");
		sbr.append("\"" + pId + "\"");
		sbr.append(",");
		sbr.append("\"name\":");
		sbr.append("\"" + name + "\"");
		sbr.append(",");
		sbr.append("\"isParent\":");
		sbr.append("\"" + (isParent ? "true" : "false") + "\"");
		sbr.append("},");
		return sbr.toString();
	}
	/**
	 * 转换map为json数据
	 * 
	 * @param map
	 * @return
	 */
	public static String toJsonByString(Map<String, String> map) {
		StringBuilder sbr = new StringBuilder();
		Set<String> set = map.keySet();
		if (set != null && set.size() > 0) {
			Iterator<String> iterator = set.iterator();
			sbr.append("{");
			while (iterator.hasNext()) {
				String type = (String) iterator.next();
				sbr.append("\"" + type + "\":");
				sbr.append("\"" + map.get(type) + "\"");
				sbr.append(",");
			}
			sbr.setCharAt(sbr.lastIndexOf(","), ' ');
			sbr.append("}");
		}
		return sbr.toString();
	}
	
	/**
	 * 解析对象集合成json对象， 可无限级嵌套集合 可包含实体类 解析实体类请注意配置 modelPackage 属性， 值为实体类的包名
	 * 
	 * @param status
	 * @param clazzs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String toJsonByBean(String status, List<?> clazzs) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("[");
		int clazzLen = clazzs.size();
		int curClzInx = 0;
		for (Object itemSender : clazzs) {
			sbr.append("{");
			// 获取类中以声明的方法
			Method[] methods = itemSender.getClass().getDeclaredMethods();
			// int methodLen = methods.length;
			// int curMetInx = 0;
			for (Method item : methods) {
				String property = item.getName();
				// 如果方法名是以get开始
				if (property.startsWith("get")) {
					String field = property.substring(property.indexOf("get") + 3, property.length()).toLowerCase();
					sbr.append("\"" + field + "\":");
					try {
						Object value = item.invoke(itemSender, new Object[] {});
						if (value != null) {
							if (value instanceof ArrayList) {
								sbr.append(wrapTwo(((ArrayList<Object>) value)));
							} else if (checkModel(value)) {
								sbr.append(wrap(value));
							} else if (value instanceof Date) {
								SimpleDateFormat format = new SimpleDateFormat(dateFormat);
								value = format.format(value);
								sbr.append("\"" + value + "\"");
							} else {
								sbr.append("\"" + value + "\"");
							}
						} else {
							sbr.append("\"\"");
						}
						sbr.append(",");
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			sbr.setCharAt(sbr.lastIndexOf(","), ' ');
			sbr.append("}");
			if (curClzInx < clazzLen - 1) {
				sbr.append(",");
			}
			curClzInx++;
		}
		sbr.append("]");
		if (status == null || status.equals("")) {
			return sbr.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"state\": ");
			sb.append("\"" + status + "\",");
			sb.append("\"data\": ");
			sb.append(sbr.toString());
			sb.append("}");
			return sb.toString();
		}
	}
	
	private static String wrapTwo(List<Object> wrap) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("[");
		int size = wrap.size();
		int curClzInx = 0;
		for (Object item : wrap) {
			sbr.append("{");
			// 获取类中以声明的方法
			Method[] methods = item.getClass().getDeclaredMethods();
			// int methodLen = methods.length;
			// int curMetInx = 0;
			for (Method other : methods) {
				String property = other.getName();
				// 如果方法名是以get开始
				if (property.startsWith("get")) {
					String field = property.substring(property.indexOf("get") + 3, property.length()).toLowerCase();
					sbr.append("\"" + field + "\":");
					try {
						Object value = other.invoke(item, new Object[] {});
						if (value != null) {
							if (value instanceof ArrayList) {
								sbr.append(wrap((ArrayList<Object>) value));
							} else if (checkModel(value)) {
								sbr.append(wrap(value));
							} else if (value instanceof Date) {
								SimpleDateFormat format = new SimpleDateFormat(dateFormat);
								value = format.format(value);
							} else {
								sbr.append("\"" + value + "\"");
							}
						} else {
							sbr.append("\"\"");
						}
						sbr.append(",");
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			sbr.setCharAt(sbr.lastIndexOf(","), ' ');
			sbr.append("}");
			if (curClzInx < size - 1) {
				sbr.append(",");
			}
			curClzInx++;
		}
		sbr.append("]");

		return sbr.toString();
	}
}
