package com.example.v15.migoproductcatalog.Model;

/**
 * Created by V15 on 19/03/2016.
 */

    public class Administrator {
        private static Administrator mInstance= null;

        public boolean flag;

        protected Administrator(){}

        public static synchronized Administrator getInstance(){
            if(null == mInstance){
                mInstance = new Administrator();
            }
            return mInstance;
        }
    }

