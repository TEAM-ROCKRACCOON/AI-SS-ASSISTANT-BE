package com.ai_ss.domain.user.api.dto.request;

public record AddressRequest(
	String roadAddressName,
	String placeDetailAddress,
	double latitude,
	double longitude
) {
}
