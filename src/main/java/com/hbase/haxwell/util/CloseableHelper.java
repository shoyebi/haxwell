package com.hbase.haxwell.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Closeable;
import java.lang.reflect.Method;

/*
 * Copyright 2019 Phaneesh Nagaraja <phaneesh.n@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class CloseableHelper {

    private static final Log log = LogFactory.getLog(CloseableHelper.class);

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable t) {
                log.error("Error closing object of type " + closeable.getClass().getName(), t);
            }
        }
    }

    public static void close(Object object) {
        if (object != null) {
            try {
                Method closeMethod = null;
                Method[] methods = object.getClass().getMethods();
                for (Method method : methods) {
                    if (method.getParameterTypes().length == 0) {
                        if (method.getName().equals("close")) {
                            closeMethod = method;
                            break;
                        } else if (method.getName().equals("shutdown")) {
                            closeMethod = method;
                        } else if (method.getName().equals("stop")) {
                            closeMethod = method;
                        }
                    }
                }

                if (closeMethod != null) {
                    closeMethod.invoke(object);
                } else {
                    log.error("Do not know how to close object of type " + object.getClass().getName());
                }
            } catch (Throwable t) {
                log.error("Error closing object of type " + object.getClass().getName(), t);
            }
        }
    }
}
