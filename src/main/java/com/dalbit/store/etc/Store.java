package com.dalbit.store.etc;

public interface Store {

    interface Platform {
        String OTHER = "101";
        String AOS_IN_APP = "010";
        String IOS_IN_APP = "001";
        String UNKNOWN = "";
    }

    interface ModeType {
        String IN_APP = "inApp";
        String OTHER = "other";
        String ALL = "all";
        String NONE = "none";
    }
    interface NationCodeType {
        String KR = "KR";
    }
    interface Screen{
        String aosMemberId = "11100000099";
        String iosMemberId = "11100000100";
    }
}
