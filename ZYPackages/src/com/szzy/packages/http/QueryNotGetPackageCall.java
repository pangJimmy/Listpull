package com.szzy.packages.http;

import java.util.List;

import com.szzy.packages.entity.GetPackages;

public interface QueryNotGetPackageCall {
	void call(List<GetPackages> listNotget);
}
