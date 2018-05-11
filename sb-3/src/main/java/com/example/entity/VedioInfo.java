package com.example.entity;

public class VedioInfo {
	/**
	 * 唯一id
	 */
	private String id;
	/**
	 * 文件名
	 */
	private String name;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 来源
	 */
	private String cite;
	/**
	 * 许可协议
	 */
	private String license;
	/**
	 * 格式
	 */
	private String format;
	/**
	 * 加密类型
	 */
	private String hashtype;
	/**
	 * md5加密值
	 */
	private String hashvalue;
	/**
	 * 文件完全限定名
	 */
	private String file;
	/**
	 * 大小
	 */
	private String size;
	/**
	 * 宽
	 */
	private String resolutionx;
	/**
	 * 高
	 */
	private String resolutiony;
	/**
	 * 标签
	 */
	private String tags;
	/**
	 * 分组
	 */
	private String group;
	/**
	 * 提供者
	 */
	private String provider;
	/**
	 * 提供日期
	 */
	private String providedate;
	
	public VedioInfo() {
		super();
	}
	public VedioInfo(String id) {
		super();
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCite() {
		return cite;
	}
	public void setCite(String cite) {
		this.cite = cite;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getHashtype() {
		return hashtype;
	}
	public void setHashtype(String hashtype) {
		this.hashtype = hashtype;
	}
	public String getHashvalue() {
		return hashvalue;
	}
	public void setHashvalue(String hashvalue) {
		this.hashvalue = hashvalue;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getResolutionx() {
		return resolutionx;
	}
	public void setResolutionx(String resolutionx) {
		this.resolutionx = resolutionx;
	}
	public String getResolutiony() {
		return resolutiony;
	}
	public void setResolutiony(String resolutiony) {
		this.resolutiony = resolutiony;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getProvidedate() {
		return providedate;
	}
	public void setProvidedate(String providedate) {
		this.providedate = providedate;
	}
	@Override
	public String toString() {
		return "VedioInfo [id=" + id + ", name=" + name + ", description=" + description + ", cite=" + cite
				+ ", license=" + license + ", format=" + format + ", hashtype=" + hashtype + ", hashvalue=" + hashvalue
				+ ", file=" + file + ", size=" + size + ", resolutionx=" + resolutionx + ", resolutiony=" + resolutiony
				+ ", tags=" + tags + ", group=" + group + ", provider=" + provider + ", providedate=" + providedate
				+ "]";
	}
	
	
	
}
