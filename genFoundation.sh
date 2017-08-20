#!/bin/sh
bindgen=bindgen/target/scala-2.11/bindgen-out

out=./out
r=/usr/include/objc
f=/System/Library/Frameworks/Foundation.framework/Headers

mkdir -p $out

## NSObject
#$bindgen -t objc -P cocoa.foundation -o $out/NSObject.scala $r/objc.h $r/NSObject.h $r/Protocol.h $f/NSObject.h $@

## NSString
#$bindgen -t objc -P cocoa.foundation -o $out/NSString.scala $f/NSString.h $@

## NSValue
#$bindgen -t objc -P cocoa.foundation -o $out/NSValue.scala $f/NSValue.h $@

## NSCoder
#$bindgen -t objc -P cocoa.foundation -o $out/NSCoder.scala $f/NSCoder.h $@

## NSError
#$bindgen -t objc -P cocoa.foundation -o $out/NSError.scala $f/NSError.h $@

## NSData
#$bindgen -t objc -P cocoa.foundation -o $out/NSData.scala $f/NSData.h $@

## NSURL
#$bindgen -t objc -P cocoa.foundation -o $out/NSURL.scala $f/NSURL.h $@

## NSCharacterSet
#$bindgen -t objc -P cocoa.foundation -o $out/NSCharacterSet.scala $f/NSCharacterSet.h $@

## NSArray
#$bindgen -t objc -P cocoa.foundation -o $out/NSArray.scala $f/NSArray.h $@

## NSDictionary
#$bindgen -t objc -P cocoa.foundation -o $out/NSDictionary.scala $f/NSDictionary.h $@

## NSProxy
#$bindgen -t objc -P cocoa.foundation -o $out/NSProxy.scala $f/NSProxy.h $@

## NSInvocation
#$bindgen -t objc -P cocoa.foundation -o $out/NSInvocation.scala $f/NSInvocation.h $@

# NSMethodSignature
$bindgen -t objc -P cocoa.foundation -o $out/NSMethodSignature.scala $f/NSMethodSignature.h $@
