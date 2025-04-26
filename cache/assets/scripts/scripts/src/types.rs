pub type Component = i32;
pub type int = i32;
pub type String = std::alloc::String;
pub type ScriptReference<'a> = &'a dyn FnOnce();

macro_rules! format {
    ($($arg:tt)*) => {{
        &""
    }};
}

macro_rules! interface {
    ($($arg:tt)*) => {{
        0
    }};
}

pub struct Array;
use std::ops::Index;
use std::ops::IndexMut;

impl Index<int> for Array {
    type Output = int;
    fn index(&self, _index: int) -> &Self::Output {
        unimplemented!()
    }
}

impl IndexMut<int> for Array {
    fn index_mut(&mut self, _index: int) -> &mut Self::Output {
        unimplemented!()
    }
}

