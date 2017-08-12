#!/bin/sh
bindgen=bindgen/target/scala-2.11/bindgen-out

out=./out
r=/usr/include/objc
f=/System/Library/Frameworks/Foundation.framework/Headers

mkdir -p $out

# NSObject
#$bindgen -t objc -P cocoa.foundation -o $out/NSObject.scala $r/objc.h $r/NSObject.h $r/Protocol.h $f/NSObject.h $@

# NSString
$bindgen -t objc -P cocoa.foundation -o $out/NSString.scala $f/NSString.h $@
