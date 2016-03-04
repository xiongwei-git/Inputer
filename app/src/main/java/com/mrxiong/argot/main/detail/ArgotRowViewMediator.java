/*
 * Copyright 2016 Ted xiong-wei@hotmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mrxiong.argot.main.detail;

import com.mrxiong.argot.framework.BaseMediator;

/**
 * Created by Ted on 2015/12/30.
 *
 * @ com.mrxiong.argot.main
 */
public interface ArgotRowViewMediator extends BaseMediator{
  void onSaveArgotResult(int result);
}
