package org.adbcj.h2.server.decoding;

import org.adbcj.DbException;
import org.adbcj.h2.decoding.H2Types;
import org.adbcj.h2.decoding.IoUtils;
import org.adbcj.h2.decoding.ResultOrWait;
import org.h2.util.DateTimeUtils;
import org.h2.value.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author roman.stoffel@gamlor.info
 */
final class ReadUtils {




    public static ResultOrWait<List<Value>> tryReadParams(DataInputStream stream,
                                                    ResultOrWait<Integer> paramsSize) throws IOException {
        if (!paramsSize.couldReadResult) {
            return ResultOrWait.WaitLonger;
        }
        List<Value> result = new ArrayList<Value>(paramsSize.result);
        ResultOrWait<Value> value = ResultOrWait.Start;
        for (int i = 0; i < paramsSize.result; i++) {
            final ResultOrWait<Integer> type = IoUtils.tryReadNextInt(stream, value);
            value = ReadUtils.tryReadValue(stream, type);
            if (value.couldReadResult) {
                result.add(value.result);
            } else {
                return ResultOrWait.WaitLonger;
            }
        }
        return ResultOrWait.result(result);
    }
    public static ResultOrWait<Value> tryReadValue(DataInputStream stream,
                                                   ResultOrWait<Integer> maybeType) throws IOException {
        if (!maybeType.couldReadResult) {
            return ResultOrWait.WaitLonger;
        }
        int typeCode = maybeType.result;
        H2Types type = H2Types.typeCodeToType(typeCode);
        switch (type) {
            case NULL:
                return ResultOrWait.result((Value) ValueNull.INSTANCE);
            case LONG:
                return convertToLong(IoUtils.tryReadNextLong(stream, maybeType));
            case DATE:
                return convertToDateValue(IoUtils.tryReadNextLong(stream, maybeType));
            case TIME:
                return convertNanoToTime(IoUtils.tryReadNextLong(stream, maybeType));
            case TIMESTAMP:
                final ResultOrWait<Long> dateValues = IoUtils.tryReadNextLong(stream, maybeType);
                final ResultOrWait<Long> nanos = IoUtils.tryReadNextLong(stream, dateValues);
                return convertToTimestampValue(dateValues, nanos);
            case INTEGER:
                return convertToInteger(IoUtils.tryReadNextInt(stream, maybeType));
            case DOUBLE:
                return convertToDouble(IoUtils.tryReadNextDouble(stream, maybeType));
            case DECIMAL:
                return convertToDecimalValue(IoUtils.tryReadNextString(stream, maybeType));
            case STRING:
                return convertToString(IoUtils.tryReadNextString(stream, maybeType));
            case CLOB:
                final ResultOrWait<Long> length = IoUtils.tryReadNextLong(stream, ResultOrWait.Start);
                if(length.couldReadResult){
                    final ResultOrWait<Value> value
                            = convertToString(IoUtils.readEncodedString(stream, length.result.intValue()));
                    final ResultOrWait<Integer> lobMagicBits = IoUtils.tryReadNextInt(stream, value);
                    if(lobMagicBits.couldReadResult){
                        return value;
                    }else{
                        return ResultOrWait.WaitLonger;
                    }

                } else{
                    return ResultOrWait.WaitLonger;
                }
            default:
                throw new DbException("Cannot handle type: " + type);
        }
    }

    static ResultOrWait<Value> convertToLong(ResultOrWait<Long> maybeValue) {
        if (!maybeValue.couldReadResult) {
            return ResultOrWait.WaitLonger;
        } else {
            return ResultOrWait.result((Value) ValueLong.get(maybeValue.result));
        }
    }

    static ResultOrWait<Value> convertToInteger(ResultOrWait<Integer> maybeValue) {
        if (!maybeValue.couldReadResult) {
            return ResultOrWait.WaitLonger;
        } else {
            return ResultOrWait.result((Value) ValueInt.get(maybeValue.result));
        }
    }

    static ResultOrWait<Value> convertToDouble(ResultOrWait<Double> maybeValue) {
        if (!maybeValue.couldReadResult) {
            return ResultOrWait.WaitLonger;
        } else {
            return ResultOrWait.result((Value) ValueDouble.get(maybeValue.result));
        }
    }

    static ResultOrWait<Value> convertToString(ResultOrWait<String> maybeValue) {
        if (!maybeValue.couldReadResult) {
            return ResultOrWait.WaitLonger;
        } else {
            return ResultOrWait.result((Value) ValueString.get(maybeValue.result));
        }
    }

    static ResultOrWait<Value> convertToDecimalValue(ResultOrWait<String> maybeValue) {
        if (!maybeValue.couldReadResult) {
            return ResultOrWait.WaitLonger;
        } else {
            return ResultOrWait.result((Value)  ValueDecimal.get(new BigDecimal(maybeValue.result)));
        }
    }

    static ResultOrWait<Value> convertNanoToTime(ResultOrWait<Long> maybeValue) {
        if (!maybeValue.couldReadResult) {
            return ResultOrWait.WaitLonger;
        } else {
            return ResultOrWait.result((Value) ValueTime.get(DateTimeUtils.convertNanoToTime(maybeValue.result)));
        }
    }

    static ResultOrWait<Value> convertToDateValue(ResultOrWait<Long> maybeValue) {
        if (!maybeValue.couldReadResult) {
            return ResultOrWait.WaitLonger;
        } else {
            return ResultOrWait.result((Value) ValueDate.get(DateTimeUtils.convertDateValueToDate(maybeValue.result)));
        }
    }

    static ResultOrWait<Value> convertToTimestampValue(ResultOrWait<Long> dateValue,ResultOrWait<Long> nanos) {
        if (dateValue.couldReadResult && nanos.couldReadResult) {
            return ResultOrWait.result((Value) ValueTimestamp.get(
                    DateTimeUtils.convertDateValueToTimestamp(dateValue.result,nanos.result)));
        } else {
            return ResultOrWait.WaitLonger;
        }
    }
}