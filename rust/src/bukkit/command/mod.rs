pub struct CommandArgs {
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
}
