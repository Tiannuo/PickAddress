package com.tikou.library_pickaddress.moddle;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/28.
 */
public class China {
    public ArrayList<Province> citylist;

    public class Province {
        public ArrayList<Area> c	;
        public String p;

        public  class Area{
            public ArrayList<Street> a;
            public String n;
            public class Street{
                public String s;
            }
        }
    }
}
