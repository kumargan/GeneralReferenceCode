CREATE FUNCTION add AS 'brickhouse.udf.collect.CombineUDF';
CREATE FUNCTION subtract AS 'brickhouse.udf.collect.SetDifferenceUDF';
CREATE FUNCTION combine_unique AS 'brickhouse.udf.collect.CombineUniqueUDAF';

CREATE FUNCTION convertIDLPStringToStruct as 'com.name.hive.udf.ConvertStringToStruct';

CREATE FUNCTION addIDLPStruct as 'com.name.hive.udf.AddIdlpSegmentInfo';

CREATE FUNCTION subtractIDLPStruct as 'com.name.hive.udf.SubtractIdlpSegmentInfo';


CREATE FUNCTION removeDuplicateIdlpSegments as 'com.name.hive.udf.RemoveDuplicateIdlpSegments';

CREATE FUNCTION removeDuplicateStringFromArray as 'com.name.hive.udf.RemoveDuplicateFromStringArray';

