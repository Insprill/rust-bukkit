use jni::JNIEnv;
use jni::objects::{JObject, JString};
use jni::sys::{jobjectArray};

use crate::get_bukkit;

pub struct CommandArgs {
    pub name: String,
    pub label: String,
    pub args: Vec<String>,
}

pub struct Command {
    pub name: String,
    executor: Option<Box<dyn Fn(&CommandArgs)>>,
    tab_completer: Option<Box<dyn Fn(&CommandArgs) -> Vec<String>>>,
}

impl Command {
    pub fn new(name: &str) -> Self {
        Self {
            name: name.to_string(),
            executor: None,
            tab_completer: None,
        }
    }

    pub fn executor(mut self, func: impl Fn(&CommandArgs) + 'static) -> Self {
        self.executor = Some(Box::new(func));
        self
    }

    pub fn tab_completer(mut self, func: impl Fn(&CommandArgs) -> Vec<String> + 'static) -> Self {
        self.tab_completer = Some(Box::new(func));
        self
    }

    pub fn register(self) {
        get_bukkit().commands.push(self);
    }
}

#[no_mangle]
pub extern "system" fn Java_net_insprill_rustbukkit_command_RustCommandHandler_execute(env: JNIEnv, _obj: JObject, j_name: JString, j_label: JString, j_args: jobjectArray) {
    let command_args = parse_command_args(env, j_name, j_label, j_args);
    for command in &get_bukkit().commands {
        if !(command.name.eq(&command_args.name)) {
            continue;
        }
        if let Some(executor) = &command.executor {
            (executor)(&command_args);
        }
        break;
    }
}

#[no_mangle]
pub extern "system" fn Java_net_insprill_rustbukkit_command_RustCommandHandler_tabComplete(env: JNIEnv, _obj: JObject, j_name: JString, j_label: JString, j_args: jobjectArray) -> jobjectArray {
    let command_args = parse_command_args(env, j_name, j_label, j_args);
    for command in &get_bukkit().commands {
        if !(command.name.eq(&command_args.name)) {
            continue;
        }
        if let Some(completer) = &command.tab_completer {
            let args: Vec<String> = (completer)(&command_args);
            let arr = env.new_object_array(args.len() as i32, "java/lang/String", JObject::null()).expect("Failed to create arg array");
            for (idx, arg) in args.iter().enumerate() {
                let arg = env.new_string(arg).expect("Failed to create java string from arg");
                env.set_object_array_element(arr, idx as i32, arg).expect("Failed to set array element");
            }
            return arr;
        }
        break;
    }
    env.new_object_array(0, "java/lang/String", JObject::null()).expect("Failed to create empty arg array")
}


fn parse_command_args(env: JNIEnv, j_name: JString, j_label: JString, j_args: jobjectArray) -> CommandArgs {
    let name: String = env.get_string(j_name).expect("Failed to get command name!").into();
    let label: String = env.get_string(j_label).expect("Failed to get command label!").into();
    let len = env.get_array_length(j_args).expect("Failed to get args length!");
    let mut args: Vec<String> = Vec::new();
    for x in 0..len {
        let element = env.get_object_array_element(j_args, x).expect("Failed to get array element");
        args.push(env.get_string(JString::from(element)).expect("Failed to get arg string!").into())
    }
    CommandArgs {
        name,
        label,
        args,
    }
}
