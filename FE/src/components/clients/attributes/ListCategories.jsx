import * as React from "react";
import List from "@mui/joy/List";
import ListItem from "@mui/joy/ListItem";
import ListSubheader from "@mui/joy/ListSubheader";
import ListItemButton from "@mui/joy/ListItemButton";
import Sheet from "@mui/joy/Sheet";
import { fetchCategories } from "~/apis/client/apiClient";

export default function ListCategories() {
  const [categories, setCategories] = React.useState([]);

  React.useEffect(() => {
    const func = async () => {
      await fetchCategories().then((res) => {
        setCategories(res.data);
      });
    };
    func();
  }, []);
  return (
    <Sheet
      variant="outlined"
      sx={{
        width: 300,
        minHeight: 360,
        maxHeight: 360,
        overflow: "auto",
        borderRadius: "sm",
        border: "none",
      }}
    >
      <ListSubheader sticky>Danh mục sản phẩm</ListSubheader>
      <List>
        {categories?.map((category, index) => (
          <ListItem key={index}>
            <ListItemButton>{category.name}</ListItemButton>
          </ListItem>
        ))}
      </List>
    </Sheet>
  );
}
