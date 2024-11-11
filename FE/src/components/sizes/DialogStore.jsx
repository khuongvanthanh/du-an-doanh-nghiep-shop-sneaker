import * as React from "react";
import {
  Button,
  DialogTitle,
  FormControl,
  FormHelperText,
  FormLabel,
  Input,
  Modal,
  ModalDialog,
  Stack,
} from "@mui/joy";
import { useForm } from "react-hook-form";

export const DialogStore = (props) => {
  const [open, setOpen] = React.useState(false);

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm();

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const onSubmit = async (item) => {
    const data = {
      name: item.name,
      length: item.length,
      width: item.width,
      sleeve: item.sleeve,
      userId: localStorage.getItem("userId"),
    };
    props.handleSubmit(data);
    handleClose();
    setValue("name", "");
    setValue("length", "");
    setValue("width", "");
    setValue("sleeve", "");
  };
  return (
    <React.Fragment>
      <Button
        variant="soft"
        onClick={handleClickOpen}
        startDecorator={props.icon}
      >
        {props.buttonTitle}
      </Button>
      <Modal open={open} onClose={() => setOpen(false)}>
        <ModalDialog>
          <DialogTitle>{props.title}</DialogTitle>
          <form onSubmit={handleSubmit(onSubmit)}>
            <Stack spacing={2} direction="row">
              <FormControl error={!!errors?.name}>
                <FormLabel required>Kích thước</FormLabel>
                <Input
                  placeholder={props.label}
                  {...register("name", {
                    required: true,
                  })}
                />
                {errors.name && (
                  <FormHelperText>Vui lòng không bỏ trống!</FormHelperText>
                )}
              </FormControl>
              <FormControl error={!!errors?.length}>
                <FormLabel required>Chiều dài</FormLabel>
                <Input
                  type="number"
                  placeholder={props.label}
                  {...register("length", {
                    required: {
                      value: true,
                      message: "Vui lòng nhập chiều dài!",
                    },
                    min: {
                      value: 1,
                      message: "Chiều dài phải lớn hơn 0",
                    },
                    max: {
                      value: 100,
                      message: "Dữ liệu không hợp lệ!",
                    },
                  })}
                />
                {errors.length && (
                  <FormHelperText>{errors.length.message}</FormHelperText>
                )}
              </FormControl>
            </Stack>
            <Stack spacing={2} direction="row" marginTop={2}>
              <FormControl error={!!errors?.width}>
                <FormLabel required>Chiều rộng</FormLabel>
                <Input
                  type="number"
                  placeholder={props.label}
                  {...register("width", {
                    required: {
                      value: true,
                      message: "Vui lòng nhập chiều rộng!",
                    },
                    min: {
                      value: 1,
                      message: "Chiều rộng phải lớn hơn 0",
                    },
                    max: {
                      value: 100,
                      message: "Dữ liệu không hợp lệ!",
                    },
                  })}
                />
                {errors.width && (
                  <FormHelperText>{errors.width.message}</FormHelperText>
                )}
              </FormControl>
              <FormControl error={!!errors?.sleeve}>
                <FormLabel required>Chiều dài lót giày</FormLabel>
                <Input
                  type="number"
                  placeholder={props.label}
                  {...register("sleeve", {
                    required: {
                      value: true,
                      message: "Vui lòng nhập chiều dài lót giày!",
                    },
                    min: {
                      value: 1,
                      message: "Chiều dài lót giày phải lớn hơn 0",
                    },
                    max: {
                      value: 100,
                      message: "Dữ liệu không hợp lệ!",
                    },
                  })}
                />
                {errors.sleeve && (
                  <FormHelperText>{errors.sleeve.message}</FormHelperText>
                )}
              </FormControl>
            </Stack>
            <Stack marginTop={2}>
              <Button type="submit">Lưu</Button>
            </Stack>
          </form>
        </ModalDialog>
      </Modal>
    </React.Fragment>
  );
};
