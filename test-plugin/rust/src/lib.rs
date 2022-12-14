use jni::JNIEnv;
use jni::objects::JObject;

use rust_bukkit::bukkit::command::Command;
use rust_bukkit::bukkit::event::{EventHandler, EventPriority};

#[no_mangle]
pub extern "system" fn Java_net_insprill_testplugin_TestPlugin_enable(env: JNIEnv, obj: JObject) {
    rust_bukkit::enable(env, obj);
    Command::new("rusty")
            .executor(|args| {
                println!("{}", args.args.join(" "))
            })
            .tab_completer(|_| {
                vec!["aye".to_string(), "yoo".to_string()]
            })
            .register();

    EventHandler::new("org.bukkit.event.player.PlayerJoinEvent")
            .executor(|_| {
                println!("Event fired!")
            })
            .ignore_cancelled(true)
            .priority(EventPriority::MONITOR)
            .register(env, obj);
}
