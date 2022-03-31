package com.dalbit.store.etc;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum StoreEnum {

    other(Store.Platform.OTHER, "other"),
    aosInApp(Store.Platform.AOS_IN_APP, "inApp"),
    iosInApp(Store.Platform.IOS_IN_APP, "inApp"),
    unknown("", "none");
    public String platform;
    public String mode;

    StoreEnum(String platform, String mode) {
        this.platform = platform;
        this.mode = mode;
    }

    private static final Map<String, StoreEnum> PLATFORM_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(StoreEnum::getPlatform, Function.identity())));

    public static StoreEnum searchFromPlatform(String platform) {
        return Optional.ofNullable(PLATFORM_MAP.get(platform)).orElse(unknown);
    }

}
