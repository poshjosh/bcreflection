
import com.bc.reflection.ReflectionUtilTest;

/*
 * Copyright 2018 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Chinomso Bassey Ikwuagwu on May 10, 2018 4:07:05 PM
 */
public class Main {

    public static void main(String... args) {
        try{
            new ReflectionUtilTest().testGetMethod();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
