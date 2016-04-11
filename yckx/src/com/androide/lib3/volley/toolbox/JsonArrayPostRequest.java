/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androide.lib3.volley.toolbox;

import com.androide.lib3.volley.AuthFailureError;
import com.androide.lib3.volley.NetworkResponse;
import com.androide.lib3.volley.ParseError;
import com.androide.lib3.volley.Request;
import com.androide.lib3.volley.Response;
import com.androide.lib3.volley.Response.ErrorListener;
import com.androide.lib3.volley.Response.Listener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class JsonArrayPostRequest extends Request<JSONArray>{
	private Listener<JSONArray> mListener;

	public JsonArrayPostRequest(String url, Listener<JSONArray> listener, ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		this.mListener = listener;
	}

	/** Default charset for JSON request. */
	protected static final String PROTOCOL_CHARSET = "utf-8";

	/**POST请求所参数*/
	@Override
	protected abstract Map<String, String> getParams() throws AuthFailureError;

	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
			return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		}
		catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
		catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONArray response) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}
}
