package com.szzy.packages.http;

import java.util.List;

import com.szzy.packages.entity.PostPackages;

/**
 * 投件查询接口
 * @author mac
 *
 */
public interface QueryPostPackageCall {
	void call(List<PostPackages> listPost);
}
