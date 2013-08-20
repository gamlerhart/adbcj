/*
 *   Copyright (c) 2007 Mike Heath.  All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.adbcj.support;

import org.adbcj.DbException;
import org.adbcj.Value;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultValue implements Value {

	private final Object value;
    private final static String[] SUPPORTED_DATE_FORMATS = {"yyyy-MM-dd HH:mm:ss.S","yyyy-MM-dd","HH:mm:ss"};

	public DefaultValue(Object value) {
		this.value = value;
	}

	public BigDecimal getBigDecimal() {
		if (value == null) {
			return null;
		}
		if (value instanceof BigDecimal) {
			return (BigDecimal)value;
		}
		return new BigDecimal(value.toString());
	}

	public boolean getBoolean() {
		if (value == null) {
			return false;
		}
		if (value instanceof Boolean) {
			return ((Boolean)value).booleanValue();
		}
		if (value instanceof Number) {
			return getInt() != 0;
		}
		return Boolean.valueOf(value.toString());
	}

	public Date getDate() {
		if (value == null) {
			return null;
		}
		if (value instanceof Date) {
			return (Date)value;
		} else if(value instanceof String){
            for(String format : SUPPORTED_DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(format).parse((String) value);
                } catch (ParseException e) {
                    // expected
                }
            }
        }
		throw new DbException(String.format("%s is not a date", value.toString()));
	}

	public double getDouble() {
		if (value == null) {
			return 0d;
		}
		if (value instanceof Number) {
			return ((Number)value).doubleValue();
		}
		return Double.valueOf(value.toString());
	}

	public float getFloat() {
		if (value == null) {
			return 0f;
		}
		if (value instanceof Number) {
			return ((Number)value).floatValue();
		}
		return Float.valueOf(value.toString());
	}

	public int getInt() {
		if (value == null) {
			return 0;
		}
		if (value instanceof Number) {
			return ((Number)value).intValue();
		}
		return Integer.valueOf(value.toString());
	}

	public long getLong() {
		if (value == null) {
			return 0L;
		}
		if (value instanceof Number) {
			return ((Number)value).longValue();
		}
		return Long.valueOf(value.toString());
	}

	public String getString() {
		return value == null ? null : value.toString();
	}

	public Object getValue() {
		return value;
	}

	public boolean isNull() {
		return value == null;
	}

    public short getShort(){
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number)value).shortValue();
        }
        return Short.valueOf(value.toString());
    }

    public byte[] getBytes() throws UnsupportedOperationException{
        if (value==null){
            return null;
        }
        try{
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            ObjectOutput out= new ObjectOutputStream(bos);
            out.writeObject(value);
            return bos.toByteArray();
        } catch (Exception e){
            throw new UnsupportedOperationException("type not supported");
        }
    }
    public byte getByte(){
        if (value == null){
            return 0;
        }
        return ((Byte)value).byteValue();
    }

	@Override
	public String toString() {
		// TODO Add padding if a display width is specified in the Field
		String s = getString();
		return s == null ? "null" : s;
	}

}
